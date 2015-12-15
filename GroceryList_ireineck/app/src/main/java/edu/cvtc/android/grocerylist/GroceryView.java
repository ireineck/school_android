package edu.cvtc.android.grocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ireineck on 12/7/15.
 */
public class GroceryView extends LinearLayout {

    private TextView groceryText;

    private GroceryItem groceryItem;

    public GroceryView(Context context, GroceryItem groceryItem) {

        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grocery_view, this, true);

        this.groceryText = (TextView)findViewById(R.id.groceryTextView);
        setGrocery(groceryItem);

    }

    public GroceryItem getGroceryItem () {
        return this.groceryItem;
    }

    public void setGrocery(GroceryItem groceryItem){

        this.groceryItem = groceryItem;
        groceryText.setText(groceryItem.getGroceryItem());

        requestLayout();
    }
}
