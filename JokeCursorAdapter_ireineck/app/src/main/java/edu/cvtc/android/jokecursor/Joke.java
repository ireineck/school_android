package edu.cvtc.android.jokecursor;

public class Joke {
	public static final int UNRATED = 0;
	public static final int LIKE = UNRATED + 1;
	public static final int DISLIKE = UNRATED + 2;

	private String m_strJoke;

	private int m_nRating;

	private String m_strAuthorName;

	private long m_nID;

	public Joke() {
		this.m_strJoke = "";
		this.m_strAuthorName = "";
		this.m_nRating = UNRATED;
	}

	public Joke(String strJoke, String strAuthor) {
		this.m_strJoke = strJoke;
		this.m_strAuthorName = strAuthor;
		this.m_nRating = UNRATED;
	}

	public Joke(String strJoke, String strAuthor, int nRating) {
		this.m_strJoke = strJoke;
		this.m_strAuthorName = strAuthor;
		this.m_nRating = nRating;
	}
	
	public Joke(String strJoke, String strAuthor, int nRating, long id) {
		this.m_strJoke = strJoke;
		this.m_strAuthorName = strAuthor;
		this.m_nRating = nRating;
		this.m_nID = id;
	}

	public String getJoke() {
		return this.m_strJoke;
	}

	public void setJoke(String strJoke) {
		this.m_strJoke = strJoke;
	}

	public int getRating() {
		return this.m_nRating;
	}

	public void setRating(int rating) {
		this.m_nRating = rating;
	}

	public String getAuthor() {
		return this.m_strAuthorName;
	}


	public void setAuthor(String strAuthor) {
		this.m_strAuthorName = strAuthor;
	}
	
	public long getID() {
		return m_nID;
	}
	
	public void setID(long id) {
		this.m_nID = id;
	}

	@Override
	public String toString() {
		return this.m_strJoke;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Joke
				&& ((Joke) obj).getID() == m_nID;
	}
}
