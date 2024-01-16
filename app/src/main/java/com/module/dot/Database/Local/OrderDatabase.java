package com.module.dot.Database.Local;

import static com.module.dot.Helpers.LocalFormat.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Items.Item;
import com.module.dot.Activities.Orders.Order;

import java.util.ArrayList;
import java.util.Objects;

public class OrderDatabase extends MyDatabaseManager {
    // Order Table
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_GLOBAL_ID = "global_id";
    private static final String ORDER_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_CREATOR_ID = "order_creator";
    private static final String ORDER_COLUMN_DATE = "order_date";
    private static final String ORDER_COLUMN_TIME = "order_time";
    private static final String ORDER_COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String ORDER_COLUMN_TOTAL_ITEM = "total_item";
    private static final String ORDER_COLUMN_STATUS = "order_status";

    private static final String NAME_TABLE_USERS = "users";
    private static final String ID_COLUMN_USERS = "user_id";

    public OrderDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db){
        // SQL query to create the "orders" table
        String query_orders = "CREATE TABLE " + ORDERS_TABLE_NAME +
                " (" + ORDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_COLUMN_GLOBAL_ID + " TEXT NOT NULL UNIQUE, " +
                ORDER_COLUMN_CREATOR_ID + " INTEGER NOT NULL, " +
                ORDER_COLUMN_DATE + " DATE, " +
                ORDER_COLUMN_TIME + " TIME, " +
                ORDER_COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, " +
                ORDER_COLUMN_TOTAL_ITEM + " INTEGER, " +
                ORDER_COLUMN_STATUS + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ORDER_COLUMN_CREATOR_ID
                + ") REFERENCES " + NAME_TABLE_USERS + " (" + ID_COLUMN_USERS + "));";

        db.execSQL(query_orders);

        Log.i("OrderDatabase", "Creating orders table...");
    }

    public long createOrder(Order newOrder){
        long newOrderId = -1;

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            if (!isValueExists(db, ORDERS_TABLE_NAME, ORDER_COLUMN_GLOBAL_ID, newOrder.getGlobalID())) {
                ContentValues cv = new ContentValues();

                String[] dateTime = getCurrentDateTime(); // Get the current date and time

                cv.put(ORDER_COLUMN_GLOBAL_ID, newOrder.getGlobalID());
                cv.put(ORDER_COLUMN_CREATOR_ID, newOrder.getCreatorID());
                cv.put(ORDER_COLUMN_TOTAL_AMOUNT, newOrder.getOrderTotalAmount());
                cv.put(ORDER_COLUMN_TOTAL_ITEM, newOrder.getOrderTotalItems());
                cv.put(ORDER_COLUMN_STATUS, newOrder.getOrderStatus());
                cv.put(ORDER_COLUMN_DATE, dateTime[0]);
                cv.put(ORDER_COLUMN_TIME, dateTime[1]);

                // Insert the order into the database
                newOrderId = db.insertOrThrow(ORDERS_TABLE_NAME, null, cv);

                if (newOrderId != -1) {
                    Log.i("MyDatabaseManager", "Order Added Successfully!");
                } else {
                    Log.i("MyDatabaseManager", "Failed to add order!");
                }
            }


        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add order: " + e.getMessage());
            throw e;
        }

        return newOrderId;

    }

    public void readOrder(ArrayList<Order> orderArrayList){
        // Get a cursor to the order data in the database
        Cursor cursor = super.readAllData(ORDERS_TABLE_NAME);

        // If the database is not empty, populate the ArrayList with order data
        while (cursor.moveToNext()) {
            String orderGlobalID = cursor.getString(1);

            ArrayList<Item> selectedItemList;

            try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(context)){
                if (!orderItemsDatabase.isTableExists("order_items")) {
                    orderItemsDatabase.onCreate(orderItemsDatabase.getWritableDatabase()); // Create the database
                    return;
                } else {
                    selectedItemList = orderItemsDatabase.readOrderItems(orderGlobalID, context);
                    Log.d("OrderDatabaseTEST", "readOrder: " + selectedItemList.size());
                }

            } catch (Exception e) {
                Log.e("OrderDatabase", "Failed to read order items: " + e.getMessage());
                throw e;
            }

            // Log selectedItemList
//            for(Item item : selectedItemList){
//                Log.i("OrderDatabaseTEST", "Item: " + item.getName());
//            }

            Order order = new Order(
                    orderGlobalID,
                    cursor.getLong(0), // Order Local ID
                    cursor.getString(3), // Order Date
                    cursor.getString(4), // Order Time
                    cursor.getString(7), // Order Status
                    cursor.getLong(6),    // Total Item
                    cursor.getDouble(5), // Total Amount
                    selectedItemList // Selected Item
            );


            orderArrayList.add(order); // Add the order to the ArrayList
        }


    }

    int getTotalItems(ArrayList<Item> selectedItemList){
        int totalItems = 0 ;

        // Find the total number of items in the order
        for(Item item : selectedItemList){
            totalItems += item.getQuantity();
        }

        return totalItems;
    }

    /**
     * Retrieves details of orders with a specific order number from a database and populates
     * them into an ArrayList of Orders objects. This method queries the provided database
     * for orders matching the given orderGlobalID. The retrieved order details are encapsulated
     * into an Orders object, and all matching orders are added to the provided ArrayList.
     *
     * @param orderGlobalID The specific order number for which details are to be retrieved.
     */
    public Order getOrderDetails(String orderGlobalID) {
        // Get a cursor to the order data in the database
        Cursor cursor = super.readAllData(ORDERS_TABLE_NAME);
        Order order = null;

        while (cursor.moveToNext()) {
            ArrayList<Item> selectedItemList;

            try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(context)){
                selectedItemList = orderItemsDatabase.readOrderItems(orderGlobalID, context);
            } catch (Exception e) {
                Log.e("OrderDatabase", "Failed to read order items: " + e.getMessage());
                throw e;
            }

            Log.d("OrderDatabaseTEST", "readOrder: " + selectedItemList.get(0).getName());

            order = new Order(
                    orderGlobalID,
                    cursor.getLong(0), // Order Local ID
                    cursor.getString(3), // Order Date
                    cursor.getString(4), // Order Time
                    cursor.getString(7), // Order Status
                    cursor.getLong(6),    // Total Item
                    cursor.getDouble(5), // Total Amount
                    selectedItemList // Selected Item
            );
        }

        cursor.close();

        return order;
    }

    public long getOrderId(String orderGlobalID) {
        long orderId = -1;
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {ORDER_COLUMN_ID};

        // Filter results WHERE "global_id" = 'orderGlobalID'
        String selection = ORDER_COLUMN_GLOBAL_ID + " = ?";
        String[] selectionArgs = {orderGlobalID};

        Cursor cursor = db.query(
                ORDERS_TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.moveToFirst()) {
            orderId = cursor.getLong(cursor.getColumnIndexOrThrow(ORDER_COLUMN_ID));
        }
        cursor.close();
        return orderId;
    }



    public void updateOrder(){

    }

    public void deleteOrder(){

    }
}
