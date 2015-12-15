package edu.cvtc.android.jokeview;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	private String m_strAuthorName;

	private ArrayList<Joke> m_arrJokeList = new ArrayList<>();

	private ListView m_vwJokeLayout;

	private EditText m_vwJokeEditText;

	private Button m_vwJokeButton;


	private ArrayList<Joke> m_arrFilteredJokeList = new ArrayList<>();
	private JokeListAdapter m_jokeAdapter;
	private Menu m_vwMenu;

	/**
	 *
	 * contextual Action Mode
	 */

	private ActionMode actionMode;
	private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			actionMode = mode;
			MenuInflater inflater = actionMode.getMenuInflater();
			inflater.inflate(R.menu.menu_remove, menu);
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

			switch (item.getItemId()){
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



	private JokeView selectedView;

	private void removeJoke(final Joke joke){
		m_arrJokeList.remove(joke);
		m_arrFilteredJokeList.remove(joke);
		m_jokeAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_strAuthorName = getResources().getString(R.string.author_name);
		m_jokeAdapter = new JokeListAdapter(this,m_arrFilteredJokeList);

		initLayout();

		String[] jokeList = this.getResources().getStringArray(R.array.jokeList);

		for (String joke : jokeList){
			addJoke(new Joke (joke, m_strAuthorName));
		}
		showAll();
		initAddJokeListeners();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		m_vwMenu = menu;
		inflater.inflate(R.menu.menu_main,m_vwMenu);
        return super.onCreateOptionsMenu(m_vwMenu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
			case R.id.submenu_like:
				filter(Joke.LIKE);
				return true;
			case R.id.submenu_dislike:
				filter(Joke.DISLIKE);
				return true;
			case R.id.submenu_unrated:
				filter(Joke.UNRATED);
				return true;
			case R.id.submenu_show_all:
				showAll();
				return true;
			case R.id.menu_filter:
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void filter(int filterType){

		m_arrFilteredJokeList.clear();

		for (Joke joke : m_arrJokeList){
			if (joke.getRating() == filterType){
				m_arrFilteredJokeList.add(joke);
			}
		}
		m_jokeAdapter.notifyDataSetChanged();
	}

	private void showAll(){
		m_arrFilteredJokeList.clear();
		m_arrFilteredJokeList.addAll(m_arrJokeList);
		m_jokeAdapter.notifyDataSetChanged();
	}

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Layout for this Activity.
	 */
	private void initLayout() {
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

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Event Listeners which will respond to requests to "Add" a new Joke to the
	 * list.
	 */
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
						case KeyEvent.KEYCODE_NUMPAD_ENTER:
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


	private void addJokeFromEditText(){
		final String jokeText = m_vwJokeEditText.getText().toString().trim();
		if (null != jokeText && !jokeText.isEmpty()){
			addJoke(new Joke(jokeText,m_strAuthorName));
		}

		m_vwJokeEditText.setText("");
	}

	private void addJoke(Joke joke) {

		m_arrJokeList.add(joke);
		m_arrFilteredJokeList.add(joke);
		m_jokeAdapter.notifyDataSetChanged();
	}
}