package edu.cvtc.android.grocerylist;


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
public class GroceryListActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private ListView groceryLayout;

    private EditText groceryEditText;

    private Button groceryButton;

    private GroceryCursorAdapter groceryAdapter;

    private GroceryView selectedView;

    protected static final String SAVED_EDIT_TEXT = "groceryEditText";

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
                    final GroceryItem selectedGroceryItem = GroceryListActivity.this.selectedView.getGroceryItem();
                    removeGroceryItem(selectedGroceryItem);
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

        this.groceryAdapter = new GroceryCursorAdapter(this, null, 0);

        initLayout();
        initAddGroceryItemListeners();

        this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        groceryEditText.setText( preferences.getString(SAVED_EDIT_TEXT, ""));
    }

    protected void initLayout() {

        setContentView(R.layout.layout_grocerylist);

        groceryButton = (Button) findViewById(R.id.newGroceryButton);
        groceryEditText = (EditText) findViewById(R.id.newGroceryEditText);

        groceryLayout = (ListView) findViewById(R.id.groceryListViewGroup);
        groceryLayout.setAdapter(groceryAdapter);
        groceryLayout.setClickable(true);
        groceryLayout.setLongClickable(true);

        groceryLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                actionMode = GroceryListActivity.this.startActionMode(actionModeCallback);
                GroceryListActivity.this.selectedView = (GroceryView) view;
                view.setSelected(true);

                return true;
            }
        });
    }

    private void initAddGroceryItemListeners() {

        groceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroceryItemFromEditText();
            }
        });

        groceryEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_BUTTON_5:
                            addGroceryItemFromEditText();
                            return true;
                        default:
                            break;
                    }
                }

                return false;
            }

        });
    }

    private void addGroceryItemFromEditText() {

        final String usersGroceryItem =  groceryEditText.getText().toString();

        if (null != usersGroceryItem && !usersGroceryItem.isEmpty()) {

            groceryEditText.setText("");
            addGroceryItem(new GroceryItem(usersGroceryItem));
            final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(groceryEditText.getWindowToken(), 0);
        }
    }

    private void addGroceryItem(GroceryItem groceryItem) {

        Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/groceryItem/" + groceryItem.getID());
        uri  = getContentResolver().insert(uri, setUpContentValues(groceryItem));

        int id = Integer.parseInt(uri.getLastPathSegment());
        groceryItem.setID(id);

        hideSoftKeyboard();

        fillData();
    }

    private void removeGroceryItem(final GroceryItem groceryItem) {

        Uri uri = Uri.parse(GroceryContentProvider.CONTENT_URI + "/groceryItem/" + groceryItem.getID());
        getContentResolver().delete(uri,null,null);
        fillData();
    }

    private void fillData(){
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        groceryLayout.setAdapter(groceryAdapter);
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
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().putString(SAVED_EDIT_TEXT, groceryEditText.getText().toString()).commit();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] projection = {
                GroceryTable.GROCERY_KEY_ID,
                GroceryTable.GROCERY_KEY_TEXT
        };

        Uri uri = GroceryContentProvider.CONTENT_URI;

        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);

        return cursorLoader;
    }


    private ContentValues setUpContentValues(GroceryItem groceryItem){

        ContentValues contentValues = new ContentValues();
        contentValues.put(GroceryTable.GROCERY_KEY_TEXT, groceryItem.getGroceryItem());

        return contentValues;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        groceryAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        groceryAdapter.swapCursor(null);
    }
}
