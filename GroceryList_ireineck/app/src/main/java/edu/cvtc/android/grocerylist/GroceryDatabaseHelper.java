package edu.cvtc.android.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by ireineck on 12/7/15.
 */
public class GroceryDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "grocery.db";

    public static final int DATABASE_VERSION = 1;

    public GroceryDatabaseHelper(Context context, String name,
                                 CursorFactory factory, int version) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        GroceryTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        GroceryTable.onUpgrade(db,oldVersion,newVersion);
    }
}
