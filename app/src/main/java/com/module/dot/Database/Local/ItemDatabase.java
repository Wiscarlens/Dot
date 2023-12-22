package com.module.dot.Database.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Items.Item;
import com.module.dot.Helpers.Utils;
import com.module.dot.R;

import java.util.ArrayList;

public class ItemDatabase extends MyDatabaseManager {
    private static final String NAME_TABLE_ITEMS = "items";
    private static final String ID_COLUMN_ITEMS = "_id";
    private static final String IMAGE_COLUMN_ITEMS = "image";
    private static final String NAME_COLUMN_ITEMS = "name";
    private static final String PRICE_COLUMN_ITEMS = "price";
    private static final String CATEGORY_ID_COLUMN_ITEMS = "category_id";
    private static final String SKU_COLUMN_ITEMS = "sku";
    private static final String UNIT_TYPE_COLUMN_ITEMS = "unit_type";
    private static final String STOCK_QUANTITY_COLUMN_ITEMS = "stock_quantity";
    private static final String WS_PRICE_COLUMN_ITEMS = "wholesales_price";
    private static final String TAX_COLUMN_ITEMS = "tax";
    private static final String DESCRIPTION_COLUMN_ITEMS = "description";
    private static final String CREATED_DATE_COLUMN_ITEMS = "create_date";

    public ItemDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void createTable(SQLiteDatabase db) {
        // SQL query to create the "items" table
        String query_items = "CREATE TABLE " + NAME_TABLE_ITEMS +
                " (" + ID_COLUMN_ITEMS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE_COLUMN_ITEMS + " BLOB, " +
                NAME_COLUMN_ITEMS + " TEXT NOT NULL, " +
                PRICE_COLUMN_ITEMS + " REAL NOT NULL, " +
                CATEGORY_ID_COLUMN_ITEMS + " TEXT, " +  // TODO: Foreign key to a future table call category
                SKU_COLUMN_ITEMS + " TEXT, " +
                UNIT_TYPE_COLUMN_ITEMS + " TEXT, " +
                STOCK_QUANTITY_COLUMN_ITEMS + " INTEGER, " +
                WS_PRICE_COLUMN_ITEMS + " REAL, " +
                TAX_COLUMN_ITEMS + " REAL, " +
                DESCRIPTION_COLUMN_ITEMS + " TEXT, " +
                CREATED_DATE_COLUMN_ITEMS + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

        // Execute the SQL query
        db.execSQL(query_items);

        Log.i("ItemDatabase", "Creating items table...");

    }

    /**
     * Inserts a new item into the database.
     *
     * @param newItem The item object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void createItem(Item newItem) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();

            if (newItem.getImage() == null) {
                // Handle the case where the conversion fails
                Log.e("MyDatabaseManager", "Failed to convert image to byte array");
                throw new SQLiteException("Failed to convert image to byte array");
            } else {
                // Convert the selected image to a byte array (Blob)
                cv.put(IMAGE_COLUMN_ITEMS, Utils.getByteArrayFromDrawable(newItem.getImage()));
            }

            cv.put(NAME_COLUMN_ITEMS, newItem.getName());
            cv.put(PRICE_COLUMN_ITEMS, newItem.getPrice());
            cv.put(CATEGORY_ID_COLUMN_ITEMS, newItem.getCategory());
            cv.put(SKU_COLUMN_ITEMS, newItem.getSku());
            cv.put(UNIT_TYPE_COLUMN_ITEMS, newItem.getUnitType());
            cv.put(STOCK_QUANTITY_COLUMN_ITEMS, newItem.getStock());
            cv.put(WS_PRICE_COLUMN_ITEMS, newItem.getWholesalePrice());
            cv.put(TAX_COLUMN_ITEMS, newItem.getTax());
            cv.put(DESCRIPTION_COLUMN_ITEMS, newItem.getDescription());

            long result = db.insertOrThrow(NAME_TABLE_ITEMS, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseManager", "Item Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add item: " + e.getMessage());
            throw e;
        }
    }


    /**
     * Populates an ArrayList with item data from the database.
     *
     * @param item_for_display The ArrayList to store Item objects for display.
     */
    public void readItem(ArrayList<Item> item_for_display) {
        // Get a cursor to the item data in the database
        Cursor cursor = super.readAllData(NAME_TABLE_ITEMS);

        // If the database is not empty, populate the arrays with the item data
        while (cursor.moveToNext()) {
            // Retrieve item image as byte array
            byte[] imageData = cursor.getBlob(1);

            // Convert the Bitmap to a Drawable if needed
            Drawable itemImageDrawable = convertByteArrayToDrawable(imageData);

            Item item = new Item(
                    cursor.getLong(0),      // id
                    cursor.getString(2),    // name
                    itemImageDrawable,         // imageData
                    cursor.getDouble(3),    // price
                    cursor.getString(5),    // SKU
                    cursor.getString(6)     // unitType
            );

            item_for_display.add(item); // Add the item to the ArrayList
        }
    }

    public void updateItem(){

    }

    public void deleteItem(){

    }

}
