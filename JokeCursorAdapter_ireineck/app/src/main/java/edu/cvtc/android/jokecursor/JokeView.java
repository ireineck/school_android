package edu.cvtc.android.jokecursor;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class JokeView extends LinearLayout implements OnCheckedChangeListener {

	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	
	private RadioGroup m_vwLikeGroup;

	private TextView m_vwJokeText;
	
	private Joke m_joke;
	
	private OnJokeChangeListener m_onJokeChangeListener;

	public JokeView(Context context, Joke joke) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.joke_view, this, true);
		this.m_vwJokeText = (TextView)findViewById(R.id.jokeTextView);
		this.m_vwLikeButton = (RadioButton)findViewById(R.id.likeButton);
		this.m_vwDislikeButton = (RadioButton)findViewById(R.id.dislikeButton);
		this.m_vwLikeGroup = (RadioGroup)findViewById(R.id.ratingRadioGroup);
		this.m_vwLikeGroup.setOnCheckedChangeListener(this);
		setJoke(joke);

		this.m_onJokeChangeListener = null;
	}

	public Joke getJoke() {
        return m_joke;
    }

	public void setJoke(Joke joke) {
		this.m_joke = joke;
		this.m_vwJokeText.setText(m_joke.getJoke());
		switch (m_joke.getRating()) {
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

	public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.likeButton:
                this.m_vwLikeButton.setChecked(true);
                m_joke.setRating(Joke.LIKE);
				notifyOnJokeChangeListener();
                break;
            case R.id.dislikeButton:
                this.m_vwDislikeButton.setChecked(true);
                m_joke.setRating(Joke.DISLIKE);
				notifyOnJokeChangeListener();
                break;
            default:
                this.m_vwLikeGroup.clearCheck();
                m_joke.setRating(Joke.UNRATED);
				notifyOnJokeChangeListener();
                break;
        }

	}

	public void setOnJokeChangeListener(OnJokeChangeListener listener) {
		this.m_onJokeChangeListener = listener;
	}

	protected void notifyOnJokeChangeListener() {
		if (null != m_onJokeChangeListener){
			m_onJokeChangeListener.onJokeChanged(this,m_joke);
		}
	}

	public static interface OnJokeChangeListener {

		public void onJokeChanged(JokeView view, Joke joke);
	}
}
