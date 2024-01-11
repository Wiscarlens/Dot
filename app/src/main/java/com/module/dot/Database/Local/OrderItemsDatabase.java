package com.module.dot.Database.Local;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Items.Item;
import com.module.dot.Helpers.Utils;

import java.util.ArrayList;

public class OrderItemsDatabase extends MyDatabaseManager {
    // Order Item Table
    private static final String ORDER_ITEMS_TABLE_NAME = "order_items";
    private static final String ORDER_ITEM_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_GLOBAL_ID = "order_global_id";
    private static final String GLOBAL_ID_COLUMN_ITEMS = "item_global_id";
    private static final String ORDER_ITEM_COLUMN_ITEM_PRICE = "item_price";
    private static final String ORDER_ITEM_COLUMN_QUANTITY = "item_quantity";

    private static final String NAME_TABLE_ITEMS = "items";
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ID_COLUMN_ITEMS = "_id";
    private static final String ORDER_COLUMN_ID = "_id";



    public OrderItemsDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void createTable(SQLiteDatabase db){
        // SQL query to create the "selected_items" table
        String query_order_items = "CREATE TABLE " + ORDER_ITEMS_TABLE_NAME +
                " (" + ORDER_ITEM_COLUMN_ID + " TEXT PRIMARY KEY, " +
                ORDER_COLUMN_GLOBAL_ID + " TEXT NOT NULL, " +
                GLOBAL_ID_COLUMN_ITEMS + " TEXT NOT NULL, " + // Add the item ID column
                ORDER_ITEM_COLUMN_ITEM_PRICE + " REAL, " +
                ORDER_ITEM_COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + ORDER_COLUMN_GLOBAL_ID +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "), " +
                " FOREIGN KEY (" + GLOBAL_ID_COLUMN_ITEMS +
                ") REFERENCES " + NAME_TABLE_ITEMS + " (" + ID_COLUMN_ITEMS + "));";

        db.execSQL(query_order_items);

        Log.i("OrderItemDatabase", "Creating orderItem table...");
    }

    public void createOrderItems (String selectedItemID, String orderGlobalID, Item item) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the specified column value already exists in the database
            if (!isValueExists(db, ORDER_ITEMS_TABLE_NAME, ORDER_ITEM_COLUMN_ID, selectedItemID)) {
                ContentValues cv = new ContentValues();

                cv.put(ORDER_ITEM_COLUMN_ID, selectedItemID);
                cv.put(ORDER_COLUMN_GLOBAL_ID, orderGlobalID);
                cv.put(GLOBAL_ID_COLUMN_ITEMS, item.getGlobalID()); // Make sure to provide the correct item ID
                cv.put(ORDER_ITEM_COLUMN_ITEM_PRICE, item.getPrice());
                cv.put(ORDER_ITEM_COLUMN_QUANTITY, item.getQuantity());

                long result = db.insertOrThrow(ORDER_ITEMS_TABLE_NAME, null, cv);

                if (result != -1) {
                    Log.i("MyDatabaseManager", "Order Item Added Successfully!");
                }
            } else {
                Log.i("MyDatabaseManager", "Order Item Already Exists!");
            }

        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add order item: " + e.getMessage());
            throw e;
        }

    }

    public ArrayList<Item> readOrderItems(String orderGlobalID){
        // Create an empty list to store the order items.
        ArrayList<Item> orderItemList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase(); // Get the database.

        String query = "SELECT i.*, oi." + ORDER_ITEM_COLUMN_QUANTITY +
                " FROM " + ORDER_ITEMS_TABLE_NAME + " oi" +
                " INNER JOIN " + NAME_TABLE_ITEMS + " i" +
                " ON oi." + GLOBAL_ID_COLUMN_ITEMS + " = i." + ID_COLUMN_ITEMS +
                " WHERE oi." + ORDER_COLUMN_GLOBAL_ID + " = '" + orderGlobalID + "'";

        Cursor cursor = db.rawQuery(query, null);   // Execute the query.

        if (cursor.moveToFirst()) {
            do {
                String itemGlobalID = cursor.getString(1); // Global ID
                String imagePath = cursor.getString(1); // Global ID
                String itemName = cursor.getString(11); // Get the item name
                double itemPrice = cursor.getDouble(3);
                Long quantity = cursor.getLong(13);

                Log.d("OrderItemDB", "ItemGlobalID: " + itemGlobalID);
                Log.d("OrderItemDB", "ItemName: " + itemName);
                Log.d("OrderItemDB", "ItemPrice: " + itemPrice);
                Log.d("OrderItemDB", "ItemQuantity: " + quantity);

                Item item = new Item(itemGlobalID, imagePath, itemName, itemPrice, quantity);
                orderItemList.add(item);

            } while (cursor.moveToNext());
        }

        // Close the cursor and database.
        cursor.close();
        db.close();

        return orderItemList;

    }
}
