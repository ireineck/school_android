package edu.cvtc.android.grocerylist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by ireineck on 12/7/15.
 */
public class GroceryCursorAdapter extends CursorAdapter {

    public GroceryCursorAdapter(Context context, Cursor groceryCursor, int flags){
        super(context, groceryCursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        GroceryItem groceryItem = new GroceryItem(cursor.getString(GroceryTable.GROCERY_COL_TEXT),
                cursor.getInt(GroceryTable.GROCERY_COL_ID));

        ((GroceryView) view).setGrocery(groceryItem);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        GroceryItem groceryItem = new GroceryItem(cursor.getString(GroceryTable.GROCERY_COL_TEXT),
                cursor.getInt(GroceryTable.GROCERY_COL_ID));

        GroceryView groceryView = new GroceryView(context, groceryItem);

        return groceryView;
    }
}
