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
    private static final String ORDER_ITEM_COLUMN_ORDER_ID = "order_id";
    private static final String ORDER_ITEM_COLUMN_ITEM_ID = "item_id";
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
                " (" + ORDER_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_ITEM_COLUMN_ORDER_ID + " INTEGER NOT NULL, " +
                ORDER_ITEM_COLUMN_ITEM_ID + " INTEGER NOT NULL, " + // Add the item ID column
                ORDER_ITEM_COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + ORDER_ITEM_COLUMN_ORDER_ID +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "), " +
                " FOREIGN KEY (" + ORDER_ITEM_COLUMN_ITEM_ID +
                ") REFERENCES " + NAME_TABLE_ITEMS + " (" + ID_COLUMN_ITEMS + "));";

        db.execSQL(query_order_items);

        Log.i("OrderItemDatabase", "Creating orderItem table...");
    }

    public void createOrderItems (long orderId, long itemId, int quantity) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues cv = new ContentValues();

            cv.put(ORDER_ITEM_COLUMN_ORDER_ID, orderId);
            cv.put(ORDER_ITEM_COLUMN_ITEM_ID, itemId); // Make sure to provide the correct item ID
            cv.put(ORDER_ITEM_COLUMN_QUANTITY, quantity);

            long result = db.insertOrThrow(ORDER_ITEMS_TABLE_NAME, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseManager", "Order Item Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add order item: " + e.getMessage());
            throw e;
        }

    }

    public ArrayList<Item> readOrderItems(long orderId, Resources resources){
        // Create an empty list to store the order items.
        ArrayList<Item> orderItemList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase(); // Get the database.

        String query = "SELECT i.*, oi." + ORDER_ITEM_COLUMN_QUANTITY +
                " FROM " + ORDER_ITEMS_TABLE_NAME + " oi" +
                " INNER JOIN " + NAME_TABLE_ITEMS + " i" +
                " ON oi." + ORDER_ITEM_COLUMN_ITEM_ID + " = i." + ID_COLUMN_ITEMS +
                " WHERE oi." + ORDER_ITEM_COLUMN_ORDER_ID + " = " + orderId;

        Cursor cursor = db.rawQuery(query, null);   // Execute the query.

        if (cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(0);
                byte[] itemImage = cursor.getBlob(1);
                String itemName = cursor.getString(2);
                double itemPrice = cursor.getDouble(3);
                int quantity = cursor.getInt(12);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable =  Utils.byteArrayToDrawable(itemImage, resources);

                Item item = new Item(itemId, itemImageDrawable, itemName, itemPrice, quantity);
                orderItemList.add(item);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database.
        cursor.close();
        db.close();

        return orderItemList;

    }
}