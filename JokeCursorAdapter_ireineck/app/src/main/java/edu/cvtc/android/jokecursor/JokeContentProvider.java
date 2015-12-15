package edu.cvtc.android.jokecursor;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class JokeContentProvider extends ContentProvider {

	private JokeDatabaseHelper databaseHelper;
	private static final int JOKE_ID = 1;
	private static final int JOKE_FILTER = 2;
	private static final String AUTHORITY = "edu.cvtc.android.jokecursor.contentprovider";
	private static final String BASE_PATH = "joke_table";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/jokes/#", JOKE_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/filters/#", JOKE_FILTER);

	}

	@Override
	public boolean onCreate() {

		this.databaseHelper = new JokeDatabaseHelper(getContext(),
				JokeDatabaseHelper.DATABASE_NAME,
				null, JokeDatabaseHelper.DATABASE_VERSION);

		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		checkColumns(projection);

		queryBuilder.setTables(JokeTable.DATABASE_TABLE_JOKE);

		int uriType = sURIMatcher.match(uri);

		switch (uriType){
			case JOKE_FILTER:
				String filter = uri.getLastPathSegment();

				if (!filter.equals(MainActivity.SHOW_ALL_FILTER_STRING)){
					queryBuilder.appendWhere(JokeTable.JOKE_KEY_RATING + "=" + filter);
				} else {
					selection = null;
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI" + uri);
		}

		SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(database, projection, selection, null, null, null, null);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase sqlDB = this.databaseHelper.getWritableDatabase();

		long id = 0;
		
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType)	{
		case JOKE_ID:
			id = sqlDB.insert(JokeTable.DATABASE_TABLE_JOKE, null, values);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();

		int rowsDeleted = 0;

		int uriType = sURIMatcher.match(uri);

		switch (uriType){
			case JOKE_ID:
				String id = uri.getLastPathSegment();

				rowsDeleted = sqLiteDatabase.delete(JokeTable.DATABASE_TABLE_JOKE,
													JokeTable.JOKE_KEY_ID + "=" + id,
													null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI" + uri);
		}

		if (rowsDeleted > 0) {
			getContext().getContentResolver().notifyChange(uri,null);
		}

		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();

		int rowsUpdated = 0;

		int uriType = sURIMatcher.match(uri);

		switch (uriType){
			case JOKE_ID:
				String id = uri.getLastPathSegment();

				if (!TextUtils.isEmpty(selection)){
					rowsUpdated = sqLiteDatabase.update(JokeTable.DATABASE_TABLE_JOKE,
														values,
														JokeTable.JOKE_KEY_ID + "=" + id + " AND " + selection,
														null);
				} else {
					rowsUpdated = sqLiteDatabase.update(JokeTable.DATABASE_TABLE_JOKE,
														values,
														JokeTable.JOKE_KEY_ID + "=" + id,
														null);
				}

				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		if (rowsUpdated > 0) {
			getContext().getContentResolver().notifyChange(uri,null);
		}


		return 0;
	}

	private void checkColumns(String[] projection) {
		final String[] available = { JokeTable.JOKE_KEY_ID, JokeTable.JOKE_KEY_TEXT, JokeTable.JOKE_KEY_RATING,
				JokeTable.JOKE_KEY_AUTHOR };
		
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
