package edu.cvtc.android.jokecursor;

import android.database.sqlite.SQLiteDatabase;

public class JokeTable {

	public static final String DATABASE_TABLE_JOKE = "joke_table";
	
	public static final String JOKE_KEY_ID = "_id";
	public static final int JOKE_COL_ID = 0;
	
	public static final String JOKE_KEY_TEXT = "joke_text";
	public static final int JOKE_COL_TEXT = JOKE_COL_ID + 1;
	
	public static final String JOKE_KEY_RATING = "rating";
	public static final int JOKE_COL_RATING = JOKE_COL_ID + 2;
	
	public static final String JOKE_KEY_AUTHOR = "author";
	public static final int JOKE_COL_AUTHOR = JOKE_COL_ID + 3;
	
	public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_JOKE + " (" +
			JOKE_KEY_ID + " integer primary key autoincrement, " + 
			JOKE_KEY_TEXT	+ " text not null, " + 
			JOKE_KEY_RATING	+ " integer not null, " + 
			JOKE_KEY_AUTHOR + " text not null);";
	
	public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_JOKE;

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
