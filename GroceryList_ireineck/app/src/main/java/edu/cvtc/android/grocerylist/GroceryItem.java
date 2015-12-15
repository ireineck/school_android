package edu.cvtc.android.grocerylist;

/**
 * Created by ireineck on 12/2/15.
 */
public class GroceryItem {

    private String groceryItem;
    private int id;

    public GroceryItem () {
        this.groceryItem = "";
    }

    public GroceryItem(String groceryItem, int id){
        this.groceryItem = groceryItem;
        this.id = id;
    }

    public GroceryItem (String groceryItem) {
        this.groceryItem = groceryItem;
    }

    public void setGroceryItem (String groceryItem){
        this.groceryItem = groceryItem;
    }

    public String getGroceryItem (){
        return this.groceryItem;
    }

    public void setID (int id) {
        this.id = id;

    }

    public int getID () {
        return id;
    }

    @Override
    public String toString() {
        return this.groceryItem;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GroceryItem
                && ((GroceryItem) obj).getID() == id;
    }
}
