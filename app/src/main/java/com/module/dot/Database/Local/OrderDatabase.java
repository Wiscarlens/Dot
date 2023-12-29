package com.module.dot.Database.Local;

import static com.module.dot.Helpers.LocalFormat.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Items.Item;
import com.module.dot.Activities.Orders.Orders;

import java.util.ArrayList;

public class OrderDatabase extends MyDatabaseManager {
    // Order Table
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_CREATOR_ID = "order_creator";
    private static final String ORDER_COLUMN_DATE = "order_date";
    private static final String ORDER_COLUMN_TIME = "order_time";
    private static final String ORDER_COLUMN_TOTAL_AMOUNT = "total_amount";
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

    protected void createTable(SQLiteDatabase db){
        // SQL query to create the "orders" table
        String query_orders = "CREATE TABLE " + ORDERS_TABLE_NAME +
                " (" + ORDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                // TODO: Make not null in the future
                ORDER_COLUMN_CREATOR_ID + " INTEGER, " +
                ORDER_COLUMN_DATE + " DATE, " +
                ORDER_COLUMN_TIME + " TIME, " +
                ORDER_COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, " +
                // TODO: not null in the future
                ORDER_COLUMN_STATUS + " TEXT, " +
                " FOREIGN KEY (" + ORDER_COLUMN_CREATOR_ID
                + ") REFERENCES " + NAME_TABLE_USERS + " (" + ID_COLUMN_USERS + "));";

        db.execSQL(query_orders);

        Log.i("OrderDatabase", "Creating orders table...");
    }

    public long createOrder(Orders newOrder){
        long newOrderId = -1;

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues cv = new ContentValues();

            String[] dateTime = getCurrentDateTime(); // Get the current date and time

            cv.put(ORDER_COLUMN_CREATOR_ID, newOrder.getCreatorID());
            cv.put(ORDER_COLUMN_TOTAL_AMOUNT, newOrder.getOrderTotalAmount());
            cv.put(ORDER_COLUMN_STATUS, newOrder.getOrderStatus());
            cv.put(ORDER_COLUMN_DATE, dateTime[0]);
            cv.put(ORDER_COLUMN_TIME, dateTime[1]);

            // Insert the order into the database
            newOrderId = db.insertOrThrow(ORDERS_TABLE_NAME, null, cv);

            if (newOrderId != -1) {
                Log.i("MyDatabaseManager", "Order Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseManager", "Failed to add order: " + e.getMessage());
            throw e;
        }

        return newOrderId;

    }

    public void readOrder(ArrayList<Orders> ordersArrayList, Resources resources){
        // Get a cursor to the order data in the database
        Cursor cursor = super.readAllData(ORDERS_TABLE_NAME);

        // If the database is not empty, populate the ArrayList with order data
        while (cursor.moveToNext()) {
            long orderNumber = cursor.getLong(0);

            ArrayList<Item> selectedItemArrayList = new ArrayList<>();

            try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(context)){
                if (!orderItemsDatabase.isTableExists("order_items")) {
                    orderItemsDatabase.onCreate(orderItemsDatabase.getWritableDatabase()); // Create the database
                } else {
                    selectedItemArrayList = orderItemsDatabase.readOrderItems(orderNumber);
                }

            } catch (Exception e) {
                Log.e("OrderDatabase", "Failed to read order items: " + e.getMessage());
                throw e;
            }

            int totalItems = 0 ;

            // Find the total number of items in the order
            for(Item item : selectedItemArrayList){
                totalItems += item.getQuantity();
            }

            Orders order = new Orders(
                    orderNumber, // Order Number
                    cursor.getString(2), // Order Date
                    cursor.getString(3), // Order Time
                    cursor.getString(5), // Order Status
                    totalItems,                     // Total Item
                    cursor.getDouble(4), // Total Amount
                    selectedItemArrayList // Selected Item
            );

            ordersArrayList.add(order); // Add the order to the ArrayList
        }


    }

    /**
     * Retrieves details of orders with a specific order number from a database and populates
     * them into an ArrayList of Orders objects. This method queries the provided database
     * for orders matching the given orderNumber. The retrieved order details are encapsulated
     * into an Orders object, and all matching orders are added to the provided ArrayList.
     *
     * @param ordersArrayList The ArrayList where the retrieved order details will be stored.
     * @param orderNumber The specific order number for which details are to be retrieved.
     */
    public void getOrdersDetails(ArrayList<Orders> ordersArrayList, long orderNumber) {
        // Get a cursor to the order data in the database
        Cursor cursor = super.readAllData(ORDERS_TABLE_NAME);

        while (cursor.moveToNext()) {
            long currentOrderNumber = cursor.getLong(0);

            if (currentOrderNumber == orderNumber) {
                ArrayList<Item> selectedItemArrayList;

                try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(context)){
                    selectedItemArrayList = orderItemsDatabase.readOrderItems(orderNumber);
                } catch (Exception e) {
                    Log.e("OrderDatabase", "Failed to read order items: " + e.getMessage());
                    throw e;
                }

                int totalItems = 0 ;

                // Find the total number of items in the order
                for(Item item : selectedItemArrayList){
                    totalItems += item.getQuantity();
                }

                Orders order = new Orders(
                        orderNumber, // Order Number
                        cursor.getString(2), // Order Date
                        cursor.getString(3), // Order Time
                        cursor.getString(5), // Order Status
                        totalItems,                     // Total Item
                        cursor.getDouble(4), // Total Amount
                        selectedItemArrayList // Selected Item
                );

                ordersArrayList.add(order); // Add the order to the ArrayList
            }
        }

    }


    public void updateOrder(){

    }

    public void deleteOrder(){

    }
}
