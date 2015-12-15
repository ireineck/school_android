package edu.cvtc.android.jokeview;

public class Joke {

    public static final int UNRATED = 0;
	public static final int LIKE = 1;
	public static final int DISLIKE = 2;

	private String m_strJoke;

	private int m_nRating;

	private String m_strAuthorName;

	public Joke() {
		m_strJoke = "";
	    m_strAuthorName = "";
        m_nRating = UNRATED;
	}

	public Joke(String strJoke, String strAuthor) {
		m_strJoke = strJoke;
		m_strAuthorName = strAuthor;
        m_nRating = UNRATED;
	}

	public Joke(String strJoke, String strAuthor, int nRating) {
		m_strJoke = strJoke;
        m_strAuthorName = strAuthor;
		m_nRating = nRating;
    }

	public String getJoke() {
		return m_strJoke;
	}

	public void setJoke(String strJoke) {
		this.m_strJoke = strJoke;
	}


	public int getRating() {
		return m_nRating;
	}

    public void setRating(int rating) {
		this.m_nRating = rating;
	}

    public String getAuthor() {
		return m_strAuthorName;
	}

    public void setAuthor(String strAuthor) {
		m_strAuthorName = strAuthor;
	}


	@Override
	public String toString() {
		return getJoke();
	}

	@Override
	public boolean equals(Object obj) {
        return obj instanceof Joke && ((Joke) obj).getJoke().equals(m_strJoke)
                && ((Joke) obj).getAuthor().equals(m_strAuthorName);
	}
}
