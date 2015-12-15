package edu.cvtc.android.jokecursor;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import edu.cvtc.android.jokecursor.JokeView.OnJokeChangeListener;

public class JokeCursorAdapter extends CursorAdapter {

	private OnJokeChangeListener m_listener;

	public JokeCursorAdapter(Context context, Cursor jokeCursor, int flags) {
		super(context, jokeCursor, flags);
		this.m_listener = null;
	}

	public void setOnJokeChangeListener(OnJokeChangeListener listener) {
		this.m_listener = listener;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		Joke joke = new Joke(cursor.getString(JokeTable.JOKE_COL_TEXT),
				cursor.getString(JokeTable.JOKE_COL_AUTHOR),
				cursor.getInt(JokeTable.JOKE_COL_RATING),
				cursor.getInt(JokeTable.JOKE_COL_ID));

		((JokeView) view).setOnJokeChangeListener(null); // stop recursive 'out of memory' issue from happening
		((JokeView) view).setJoke(joke);
		((JokeView) view).setOnJokeChangeListener(this.m_listener);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		Joke joke = new Joke(cursor.getString(JokeTable.JOKE_COL_TEXT),
				cursor.getString(JokeTable.JOKE_COL_AUTHOR),
				cursor.getInt(JokeTable.JOKE_COL_RATING),
				cursor.getInt(JokeTable.JOKE_COL_ID));

		JokeView jokeView = new JokeView(context, joke);
		jokeView.setOnJokeChangeListener(this.m_listener);

		return jokeView;
	}
}