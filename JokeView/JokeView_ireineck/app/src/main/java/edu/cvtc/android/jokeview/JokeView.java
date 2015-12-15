package edu.cvtc.android.jokeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


public class JokeView extends LinearLayout implements OnCheckedChangeListener {

	/** Radio buttons for liking or disliking a joke. */
	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	
	/** The container for the radio buttons. */
	private RadioGroup m_vwLikeGroup;

	/** Displays the joke text. */
	private TextView m_vwJokeText;
	
	/** The data version of this View, containing the joke's information. */
	private Joke m_joke;

	/**
	 * Basic Constructor that takes only an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added. 
	 *            
	 * @param joke
	 * 			  The Joke this view is responsible for displaying.
	 */
	public JokeView(Context context, Joke joke) {

		super(context);

		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.joke_view, this, true);

		m_vwLikeButton = (RadioButton) findViewById(R.id.likeButton);
		m_vwDislikeButton = (RadioButton) findViewById(R.id.dislikeButton);
		m_vwJokeText = (TextView) findViewById(R.id.jokeTextView);
		m_vwLikeGroup = (RadioGroup) findViewById(R.id.ratingRadioGroup);

		m_vwLikeGroup.setOnCheckedChangeListener(this);

		setJoke(joke);
	}

	public Joke getJoke(){
		return m_joke;
	}

	/**
	 * Mutator method for changing the Joke object this View displays. This View
	 * will be updated to display the correct contents of the new Joke.
	 * 
	 * @param joke
	 *            The Joke object which this View will display.
	 */
	public void setJoke(Joke joke) {

		this.m_joke = joke;
		this.m_vwJokeText.setText(m_joke.getJoke());

		switch (m_joke.getRating()){
			case Joke.LIKE:
				m_vwLikeButton.setChecked(true);
				break;
			case Joke.DISLIKE:
				m_vwDislikeButton.setChecked(true);
				break;
			default:
				break;

		}
		requestLayout();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.likeButton:
				this.m_vwLikeButton.setChecked(true);
				m_joke.setRating(Joke.LIKE);
				break;
			case R.id.dislikeButton:
				this.m_vwDislikeButton.setChecked(true);
				m_joke.setRating(Joke.DISLIKE);
				break;
			default:
				m_vwLikeGroup.clearCheck();
				m_joke.setRating(Joke.UNRATED);
				break;
		}
	}
}
