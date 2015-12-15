package edu.cvtc.android.jokecursor;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, JokeView.OnJokeChangeListener {

    private String m_strAuthorName;

    private ListView m_vwJokeLayout;

    private EditText m_vwJokeEditText;

    private Button m_vwJokeButton;

    private JokeCursorAdapter m_jokeAdapter;
    private Menu m_vwMenu;

    private static final int FILTER = Menu.FIRST;
    private static final int FILTER_LIKE = SubMenu.FIRST;
    private static final int FILTER_DISLIKE = SubMenu.FIRST + 1;
    private static final int FILTER_UNRATED = SubMenu.FIRST + 2;
    private static final int FILTER_SHOW_ALL = SubMenu.FIRST + 3;

    public static final String SHOW_ALL_FILTER_STRING = "" + FILTER_SHOW_ALL;

    private JokeView selectedView;

    protected int m_nFilter;

	protected static final String SAVED_FILTER_VALUE = "m_nFilter";
	
	protected static final String SAVED_EDIT_TEXT = "m_vwJokeEditText";

    private static final int LOADER_ID = 1;

    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.actionmenu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    final Joke selectedJoke = MainActivity.this.selectedView.getJoke();
                    removeJoke(selectedJoke);
                    mode.finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.m_jokeAdapter = new JokeCursorAdapter(this, null, 0);
        this.m_jokeAdapter.setOnJokeChangeListener(this);
		this.m_strAuthorName = this.getResources().getString(R.string.author_name);
        m_nFilter = FILTER_SHOW_ALL;

        initLayout();
		initAddJokeListeners();

        this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        m_vwJokeEditText.setText( preferences.getString(SAVED_EDIT_TEXT, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        m_vwMenu = menu;
        inflater.inflate(R.menu.mainmenu, m_vwMenu);
        return true;
    }

    protected void initLayout() {
        setContentView(R.layout.advanced);

        m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
        m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);

        m_vwJokeLayout = (ListView) findViewById(R.id.jokeListViewGroup);
        m_vwJokeLayout.setAdapter(m_jokeAdapter);
        m_vwJokeLayout.setClickable(true);
        m_vwJokeLayout.setLongClickable(true);

        m_vwJokeLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                actionMode = MainActivity.this.startActionMode(actionModeCallback);
                MainActivity.this.selectedView = (JokeView) view;
                view.setSelected(true);
                return true;
            }
        });
	}
    private void initAddJokeListeners() {

        m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJokeFromEditText();
            }
        });

        m_vwJokeEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_BUTTON_5:
                            addJokeFromEditText();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }

        });
    }

    private void addJokeFromEditText() {
        final String usersJoke =  m_vwJokeEditText.getText().toString();
        if (null != usersJoke && !usersJoke.isEmpty()) {
            m_vwJokeEditText.setText("");
            addJoke(new Joke(usersJoke, m_strAuthorName));
            final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
        }
    }

    private void addJoke(Joke joke) {

        Uri uri = Uri.parse(JokeContentProvider.CONTENT_URI + "/jokes/" + joke.getID());
        uri  = getContentResolver().insert(uri, setUpContentValues(joke));

        int id = Integer.parseInt(uri.getLastPathSegment());
        joke.setID(id);

        hideSoftKeyboard();

        fillData();
    }

    private void removeJoke(final Joke joke) {
        Uri uri = Uri.parse(JokeContentProvider.CONTENT_URI + "/jokes/" + joke.getID());
        getContentResolver().delete(uri, null, null);
        fillData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submenu_like:
                filter(FILTER_LIKE);
                return true;
            case R.id.submenu_dislike:
                filter(FILTER_DISLIKE);
                return true;
            case R.id.submenu_unrated:
                filter(FILTER_UNRATED);
                return true;
            case R.id.submenu_show_all:
              filter(FILTER_SHOW_ALL);
                return true;
            case R.id.menu_filter:
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void filter(int filterType) {
        m_nFilter = filterType;
        setMenuTitle();
        fillData();
    }

    private void fillData(){
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        m_vwJokeLayout.setAdapter(m_jokeAdapter);
    }

    private void setMenuTitle() {
        m_vwMenu.getItem(0).setTitle(getMenuTitleChange());
    }

    private void hideSoftKeyboard() {
      final View currentFocus = this.getCurrentFocus();
        if (null != currentFocus){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_FILTER_VALUE,m_nFilter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        m_nFilter = savedInstanceState.getInt(SAVED_FILTER_VALUE);

        filter(m_nFilter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(getMenuTitleChange());
        m_vwMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

   private String getMenuTitleChange() {
       switch (m_nFilter) {
           case FILTER_LIKE:
               return getResources().getString(R.string.like_menuitem);
           case FILTER_DISLIKE:
               return getResources().getString(R.string.dislike_menuitem);
           case FILTER_UNRATED:
               return getResources().getString(R.string.unrated_menuitem);
           default:
               return getResources().getString(R.string.show_all_menuitem);
       }
   }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().putString(SAVED_EDIT_TEXT, m_vwJokeEditText.getText().toString()).commit();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] projection = {JokeTable.JOKE_KEY_ID,
                JokeTable.JOKE_KEY_TEXT,
                JokeTable.JOKE_KEY_RATING,
                JokeTable.JOKE_KEY_AUTHOR};

        Uri uri = Uri.parse(JokeContentProvider.CONTENT_URI + "/filters/" + getFilterString());

        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);

        return cursorLoader;
    }

    private String getFilterString () {
        switch (m_nFilter) {
            case FILTER_LIKE:
                return "" + Joke.LIKE;
            case FILTER_DISLIKE:
                return "" + Joke.DISLIKE;
            case FILTER_UNRATED:
                return "" + Joke.UNRATED;
            default:
                return SHOW_ALL_FILTER_STRING;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        m_jokeAdapter.swapCursor(data);
        m_jokeAdapter.setOnJokeChangeListener(this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        m_jokeAdapter.swapCursor(null);
    }

    @Override
    public void onJokeChanged(JokeView view, Joke joke) {
        Uri uri = Uri.parse(JokeContentProvider.CONTENT_URI + "/jokes/" + joke.getID());

        getContentResolver().update(uri, setUpContentValues(joke), null, null);
        m_jokeAdapter.setOnJokeChangeListener(null);

        fillData();

    }

    private ContentValues setUpContentValues(Joke joke){
        ContentValues contentValues = new ContentValues();
        contentValues.put(JokeTable.JOKE_KEY_TEXT, joke.getJoke());
        contentValues.put(JokeTable.JOKE_KEY_RATING, joke.getRating());
        contentValues.put(JokeTable.JOKE_KEY_AUTHOR, joke.getAuthor());
        return contentValues;
    }
}