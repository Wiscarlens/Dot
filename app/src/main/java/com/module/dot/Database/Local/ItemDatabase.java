package com.module.dot.Database.Local;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Items.Items;
import com.module.dot.Activities.Users.Users;
import com.module.dot.Helpers.Utils;
import com.module.dot.R;

import java.util.ArrayList;

public class ItemDatabase extends MyDatabaseHelper {
    private static final String ITEMS_TABLE = "items";
    private static final String ITEMS_COLUMN_ID = "_id";
    private static final String ITEMS_COLUMN_IMAGE = "image";
    private static final String ITEMS_COLUMN_NAME = "name";
    private static final String ITEMS_COLUMN_PRICE = "price";
    private static final String ITEMS_COLUMN_CATEGORY_ID = "category_id";
    private static final String ITEMS_COLUMN_SKU = "sku";
    private static final String ITEMS_COLUMN_TYPE = "unit_type";
    private static final String ITEMS_COLUMN_STOCK_QUANTITY = "stock_quantity";
    private static final String ITEMS_COLUMN_WS_PRICE = "wholesales_price";
    private static final String ITEMS_COLUMN_TAX = "tax";
    private static final String ITEMS_COLUMN_DESCRIPTION = "description";
    private static final String ITEMS_COLUMN_CREATED_DATE = "create_date";

    public ItemDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the "items" table
        String query_items = "CREATE TABLE " + ITEMS_TABLE +
                " (" + ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEMS_COLUMN_IMAGE + " BLOB, " +
                // Need to be a unique value
                ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                ITEMS_COLUMN_PRICE + " REAL NOT NULL, " +
                ITEMS_COLUMN_CATEGORY_ID + " TEXT, " +  // TODO: Foreign key to a future table call category
                ITEMS_COLUMN_SKU + " TEXT, " +
                ITEMS_COLUMN_TYPE + " TEXT, " +
                ITEMS_COLUMN_STOCK_QUANTITY + " INTEGER, " +
                ITEMS_COLUMN_WS_PRICE + " REAL, " +
                ITEMS_COLUMN_TAX + " REAL, " +
                ITEMS_COLUMN_DESCRIPTION + " TEXT, " +
                ITEMS_COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    }

    /**
     * Inserts a new item into the database.
     *
     * @param newItem The item object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void createItem(Items newItem)
            throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(ITEMS_COLUMN_IMAGE, Utils.getByteArrayFromDrawable(newItem.getImage())); // Convert the selected image to a byte array (Blob)
            cv.put(ITEMS_COLUMN_NAME, newItem.getName());
            cv.put(ITEMS_COLUMN_PRICE, newItem.getPrice());
            cv.put(ITEMS_COLUMN_CATEGORY_ID, newItem.getCategory());
            cv.put(ITEMS_COLUMN_SKU, newItem.getSku());
            cv.put(ITEMS_COLUMN_TYPE, newItem.getUnitType());
            cv.put(ITEMS_COLUMN_STOCK_QUANTITY, newItem.getStock());
            cv.put(ITEMS_COLUMN_WS_PRICE, newItem.getWholesalePrice());
            cv.put(ITEMS_COLUMN_TAX, newItem.getTax());
            cv.put(ITEMS_COLUMN_DESCRIPTION, newItem.getDescription());

            long result = db.insertOrThrow(ITEMS_TABLE, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseHelper", "Item Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseHelper", "Failed to add item: " + e.getMessage());
            throw e;
        }


    }

    /**
     * Populates an ArrayList with item data from the database and updates the UI accordingly.
     *
     * @param myDB The database helper instance for querying item data.
     * @param items_for_display The ArrayList to store Items objects for display.
     * @param itemGridview The GridView UI element to display items.
     * @param noDataImage The ImageView UI element to show when no data is available.
     * @param noDataText The TextView UI element to show when no data is available.
     * @param resources The Resources instance to access app resources.
     */
    public void readItem(com.module.dot.Database.MyDatabaseHelper myDB, ArrayList<Items> items_for_display,
                         GridView itemGridview, ImageView noDataImage,
                         TextView noDataText, Resources resources){
        {
            // Get a cursor to the item data in the database
            Cursor cursor = super.readAllData(ITEMS_TABLE);

            // Check if the database is empty
            if (cursor.getCount() == 0) {
                // If the database is empty, hide the item grid view and show the no data message
                itemGridview.setVisibility(View.GONE);
                noDataImage.setVisibility(View.VISIBLE);
                noDataText.setVisibility(View.VISIBLE);

            } else {
                // If the database is not empty, populate the arrays with the item data
                while (cursor.moveToNext()) {
                    // Retrieve item image as byte array
                    byte[] imageData = cursor.getBlob(1);

                    // Convert the image byte array to a Bitmap
                    Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    // Convert the Bitmap to a Drawable if needed
                    Drawable itemImageDrawable = new BitmapDrawable(resources, itemImageBitmap);

                    Items item = new Items(
                            cursor.getLong(0),      // id
                            cursor.getString(2),    // name
                            itemImageDrawable,                 // imageData
                            cursor.getDouble(3),    // price
                            cursor.getString(5),    // SKU
                            cursor.getString(6),    // unitType
                            // TODO: Default Background Color
                            R.color.white                      // backgroundColor
                    );

                    items_for_display.add(item); // Add the item to the ArrayList
                }

                // Show the item grid view and hide the no data message
                itemGridview.setVisibility(View.VISIBLE);
                noDataImage.setVisibility(View.GONE);
                noDataText.setVisibility(View.GONE);
            }
        }
    }

    public void updateItem(){

    }

    public void deleteItem(){

    }
}
