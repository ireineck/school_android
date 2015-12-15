package edu.cvtc.android.grocerylist;

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

/**
 * Created by ireineck on 12/7/15.
 */
public class GroceryContentProvider extends ContentProvider {

    private GroceryDatabaseHelper databaseHelper;

    private static final int GROCERY_ID = 1;

    private static final String AUTHORITY = "edu.cvtc.android.grocerylist.contentprovider";

    private static final String BASE_PATH = "grocery_table";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uRIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uRIMatcher.addURI(AUTHORITY, BASE_PATH + "/groceryItem/#",GROCERY_ID  );

    }

    @Override
    public boolean onCreate() {

        this.databaseHelper = new GroceryDatabaseHelper(getContext(), GroceryDatabaseHelper.DATABASE_NAME,
                null, GroceryDatabaseHelper.DATABASE_VERSION);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)  {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        queryBuilder.setTables(GroceryTable.DATABASE_TABLE_GROCERY);

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

        int uriType = uRIMatcher.match(uri);

        switch (uriType) {
            case GROCERY_ID:
                id = sqlDB.insert(GroceryTable.DATABASE_TABLE_GROCERY, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);


        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();

        int rowsDeleted = 0;

        int uriType = uRIMatcher.match(uri);

        switch (uriType){
            case GROCERY_ID:
                String id = uri.getLastPathSegment();

                rowsDeleted = sqLiteDatabase.delete(GroceryTable.DATABASE_TABLE_GROCERY,
                        GroceryTable.GROCERY_KEY_ID + "=" + id,
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
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();

        int rowsUpdated = 0;

        int uriType = uRIMatcher.match(uri);

        switch (uriType){
            case GROCERY_ID:
                String id = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)){
                    rowsUpdated = sqLiteDatabase.update(GroceryTable.DATABASE_TABLE_GROCERY,
                            values,
                            GroceryTable.GROCERY_KEY_ID + "=" + id + " AND " + selection,
                            null);
                } else {
                    rowsUpdated = sqLiteDatabase.update(GroceryTable.DATABASE_TABLE_GROCERY,
                            values,
                            GroceryTable.GROCERY_KEY_ID + "=" + id,
                            null);
                }

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }


        return 0;    }

    private void checkColumns(String[] projection) {
        final String[] available = { GroceryTable.GROCERY_KEY_ID, GroceryTable.GROCERY_KEY_TEXT };

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
