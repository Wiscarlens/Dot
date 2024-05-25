package com.module.dot.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.model.Item;

import java.util.ArrayList;

public class ItemDatabase extends MyDatabaseManager {
    private static final String NAME_TABLE_ITEMS = "items";
    private static final String ID_COLUMN_ITEMS = "_id";
    private static final String GLOBAL_ID_COLUMN_ITEMS = "global_id";
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
                GLOBAL_ID_COLUMN_ITEMS + " TEXT NOT NULL UNIQUE, " +
                IMAGE_COLUMN_ITEMS + " TEXT, " +
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
     * Inserts a new item into the database if the specified column value does not exist.
     *
     * @param newItem     The item object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void createItem(Item newItem) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the specified column value already exists in the database
            if (!isValueExists(db, NAME_TABLE_ITEMS, GLOBAL_ID_COLUMN_ITEMS, newItem.getGlobalID())) {
                ContentValues cv = new ContentValues();

                cv.put(GLOBAL_ID_COLUMN_ITEMS, newItem.getGlobalID());
                cv.put(IMAGE_COLUMN_ITEMS, newItem.getImagePath());
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
            } else {
                Log.i("MyDatabaseManager", "Item with specified value already exists in the column!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add item: " + e.getMessage());
            throw e;
        }
    }



    /**
     * Populates an ArrayList with item data from the database.
     *
     * @param itemList The ArrayList to store Item objects for display.
     */
    public void readItem(ArrayList<Item> itemList) {
        // Get a cursor to the item data in the database
        Cursor cursor = super.readAllData(NAME_TABLE_ITEMS);

        try {
            // If the database is not empty, populate the arrays with the item data
            while (cursor.moveToNext()) {

                Item item = new Item(
                        cursor.getLong(0),      // local id
                        cursor.getString(1),    // global id
                        cursor.getString(2),    // imagePath
                        cursor.getString(3),    // name
                        cursor.getDouble(4),    // price
                        cursor.getDouble(10),    // tax
                        cursor.getString(6),    // SKU
                        cursor.getString(7)     // unitType
                );

                itemList.add(item); // Add the item to the ArrayList
            }
        } finally {
            // Ensure the Cursor is closed to free up resources
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public String getItemName(String itemGlobalID) {
        String itemName = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + NAME_COLUMN_ITEMS + " FROM " + NAME_TABLE_ITEMS + " WHERE " + GLOBAL_ID_COLUMN_ITEMS + " = ?";
            cursor = db.rawQuery(query, new String[]{itemGlobalID});

            if (cursor.moveToFirst()) {
                itemName = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("ItemDatabase", "Failed to get item name: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return itemName;
    }

    public void updateItem(){

    }

    public void deleteItem(){

    }

}
