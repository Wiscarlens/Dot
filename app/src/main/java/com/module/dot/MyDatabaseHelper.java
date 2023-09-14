package com.module.dot;

/*
 Created by Wiscarlens Lucius on 1 August 2023.
 */

import static com.module.dot.LocalFormat.getCurrentDateTime;
import static com.module.dot.PasswordUtils.hashPassword;
import static com.module.dot.Utils.byteArrayToDrawable;
import static com.module.dot.Utils.getByteArrayFromDrawable;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "Module.db";
    private static final int DATABASE_VERSION = 1;

    // User Table
    private static final String USERS_TABLE_NAME = "users";
    private static final String USERS_COLUMN_ID = "user_id";
    private static final String USERS_COLUMN_FIRST_NAME = "first_name";
    private static final String USERS_COLUMN_MIDDLE_NAME = "middle_name";
    private static final String USERS_COLUMN_LAST_NAME = "last_name";
    private static final String USERS_COLUMN_DOB = "date_of_birth";
    private static final String USERS_COLUMN_GENDER = "gender";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String USERS_COLUMN_STREET_NAME = "street_name";
    private static final String USERS_COLUMN_CITY = "city";
    private static final String USERS_COLUMN_STATE = "state";
    private static final String USERS_COLUMN_ZIP_CODE = "zip_code";
    private static final String USERS_COLUMN_PROFILE_IMAGE = "profile_picture";
    private static final String USERS_COLUMN_POSITION = "position";
    private static final String USERS_COLUMN_PASSWORD = "password_hash";
    // TODO: new database column to be added
    private static final String USERS_COLUMN_DATE_REGISTERED = "date_registered";

    // Items Table
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

    // Order Table
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_CREATOR_ID = "order_creator";
    private static final String ORDER_COLUMN_DATE = "order_date";
    private static final String ORDER_COLUMN_TIME = "order_time";
    private static final String ORDER_COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String ORDER_COLUMN_STATUS = "order_status";

    // Order Items Table
    private static final String ORDER_ITEMS_TABLE_NAME = "order_items";
    private static final String ORDER_ITEM_COLUMN_ID = "_id";
    private static final String ORDER_ITEM_COLUMN_ORDER_ID = "order_id";
    private static final String ORDER_ITEM_COLUMN_ITEM_ID = "item_id";
    private static final String ORDER_ITEM_COLUMN_QUANTITY = "item_quantity";

    // Transaction Table
    private static final String TRANSACTION_TABLE = "transactions";
    private static final String TRANSACTION_COLUMN_ID = "_id";
    // TODO: change it to order number
    private static final String TRANSACTION_COLUMN_ORDER_NUMBER = "order_id";
    private static final String TRANSACTION_COLUMN_PAYMENT_DATE = "payment_date";
    private static final String TRANSACTION_COLUMN_PAYMENT_TIME = "payment_time";
    private static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    private static final String TRANSACTION_COLUMN_STATUS = "status";
    private static final String TRANSACTION_COLUMN_PAYMENT_METHOD = "payment_method";


    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the "users" table
        String query_users = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                USERS_COLUMN_MIDDLE_NAME + " TEXT, " +
                USERS_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                USERS_COLUMN_DOB + " DATE, " +
                USERS_COLUMN_GENDER + " TEXT, " +
                USERS_COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                USERS_COLUMN_PHONE_NUMBER + " TEXT, " +
                USERS_COLUMN_STREET_NAME + " TEXT, " +
                USERS_COLUMN_CITY + " TEXT, " +
                USERS_COLUMN_STATE + " TEXT, " +
                USERS_COLUMN_ZIP_CODE + " TEXT, " +
                USERS_COLUMN_PROFILE_IMAGE + " BLOB, " +
                USERS_COLUMN_POSITION + " TEXT, " +
                USERS_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                USERS_COLUMN_DATE_REGISTERED + " TIME DEFAULT CURRENT_TIMESTAMP);";

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
                + ") REFERENCES " + USERS_TABLE_NAME + " (" + USERS_COLUMN_ID + "));";

        // SQL query to create the "selected_items" table
        String query_order_items = "CREATE TABLE " + ORDER_ITEMS_TABLE_NAME +
                " (" + ORDER_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_ITEM_COLUMN_ORDER_ID + " INTEGER NOT NULL, " +
                ORDER_ITEM_COLUMN_ITEM_ID + " INTEGER NOT NULL, " + // Add the item ID column
                ORDER_ITEM_COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + ORDER_ITEM_COLUMN_ORDER_ID +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "), " +
                " FOREIGN KEY (" + ORDER_ITEM_COLUMN_ITEM_ID +
                ") REFERENCES " + ITEMS_TABLE + " (" + ITEMS_COLUMN_ID + "));";

        // SQL query to create the "transactions" table
        String query_transactions = "CREATE TABLE " + TRANSACTION_TABLE +
                " (" + TRANSACTION_COLUMN_ID + " TEXT PRIMARY KEY, " +
                TRANSACTION_COLUMN_ORDER_NUMBER + " INTEGER NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_DATE + " DATE, " +
                TRANSACTION_COLUMN_PAYMENT_TIME + " TIME, " +
                TRANSACTION_COLUMN_AMOUNT + " REAL NOT NULL, " +
                TRANSACTION_COLUMN_STATUS + " TEXT NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_METHOD + " TEXT, " +
                " FOREIGN KEY (" + TRANSACTION_COLUMN_ORDER_NUMBER +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "));";

        db.execSQL(query_users);
        db.execSQL(query_items);
        db.execSQL(query_orders);
        db.execSQL(query_order_items);
        db.execSQL(query_transactions);
    }


    /**
     * Inserts a new item into the database.
     *
     * @param newItem The item object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void setItem(Items newItem)
            throws SQLiteException {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(ITEMS_COLUMN_IMAGE, getByteArrayFromDrawable(newItem.getImage())); // Convert the selected image to a byte array (Blob)
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
     * Inserts or updates user information in the database.
     * This method allows you to set or update user details in the database.
     * It first checks if the email already exists in the database to prevent duplicate entries.
     * If the email already exists, it displays a toast message and does not perform the insertion.
     *
     * @param newUsers The Users object containing the user's information to be added or updated.
     * @throws SQLiteException If there is an error with the SQLite database operations, an exception is thrown.
     */
    public void setUser(Users newUsers)
            throws SQLiteException {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the email already exists in the database
            if (emailExists(db, newUsers.getEmail())) {
                Log.i("MyDatabaseHelper", "Email already exists.");
                Toast.makeText(context, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues cv = new ContentValues();

            cv.put(USERS_COLUMN_FIRST_NAME, newUsers.getFirstName());
            cv.put(USERS_COLUMN_MIDDLE_NAME, newUsers.getMiddleName());
            cv.put(USERS_COLUMN_LAST_NAME, newUsers.getLastName());
            cv.put(USERS_COLUMN_DOB, newUsers.getDateOfBirth());
            cv.put(USERS_COLUMN_GENDER, newUsers.getGender());
            cv.put(USERS_COLUMN_EMAIL, newUsers.getEmail());
            cv.put(USERS_COLUMN_PHONE_NUMBER, newUsers.getPhoneNumber());
            cv.put(USERS_COLUMN_STREET_NAME, newUsers.getStreetName());
            cv.put(USERS_COLUMN_CITY, newUsers.getCity());
            cv.put(USERS_COLUMN_STATE, newUsers.getState());
            cv.put(USERS_COLUMN_ZIP_CODE, newUsers.getZipCode());
            cv.put(USERS_COLUMN_PROFILE_IMAGE, getByteArrayFromDrawable(newUsers.getProfileImage()));
            cv.put(USERS_COLUMN_POSITION, newUsers.getPosition());
            cv.put(USERS_COLUMN_PASSWORD, hashPassword(newUsers.getPassword()));

            long result = db.insertOrThrow(USERS_TABLE_NAME, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseHelper", "User Added Successfully!");
            }
        } catch (SQLiteException e) {
            e.printStackTrace(); // Log the error stack trace for debugging
            Log.e("MyDatabaseHelper", "Failed to add user: " + e.getMessage());
            throw e;
        }
    }



    /**
     * Inserts a new order into the database.
     *
     * @param newOrder The order object containing information to be added.
     * @return The ID of the newly inserted order, or -1 if insertion failed.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public long setOrder(Orders newOrder)
            throws SQLiteException {
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
                Log.i("MyDatabaseHelper", "Order Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseHelper", "Failed to add order: " + e.getMessage());
            throw e;
        }

        return newOrderId;
    }

    /**
     * Adds an order item to the database associated with the specified order and item.
     *
     * @param orderId   The ID of the order to which the item belongs.
     * @param itemId    The ID of the item being added to the order.
     * @param quantity  The quantity of the item being added to the order.
     * @throws SQLiteException If there is an error while interacting with the SQLite database.
     */
    public void setOrderItem(long orderId, long itemId, int quantity)
            throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues cv = new ContentValues();

            cv.put(ORDER_ITEM_COLUMN_ORDER_ID, orderId);
            cv.put(ORDER_ITEM_COLUMN_ITEM_ID, itemId); // Make sure to provide the correct item ID
            cv.put(ORDER_ITEM_COLUMN_QUANTITY, quantity);

            long result = db.insertOrThrow(ORDER_ITEMS_TABLE_NAME, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseHelper", "Order Item Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseHelper", "Failed to add order item: " + e.getMessage());
            throw e;
        }
    }

    // write me a method that check if a transaction id is in the database
    // if it is in the database, generate a new transaction id
    // if it is not in the database, return the transaction id


    /**
     * Inserts a new transaction record into the database.
     *
     * @param newTransaction The transaction object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void setTransaction(Transactions newTransaction)
            throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues cv = new ContentValues();

            String transactionId;

            // Generate a new Transaction ID and check if it exists in the database
            do {
                transactionId = Utils.generateTransactionNumber();
            } while (doesTransactionIdExist(db, transactionId));

            Log.d("MyDatabaseHelper", "Transaction ID: " + transactionId);

            String[] dateTime = getCurrentDateTime(); // Get the current date and time

            cv.put(TRANSACTION_COLUMN_ID, transactionId);
            cv.put(TRANSACTION_COLUMN_PAYMENT_DATE, dateTime[0]);
            cv.put(TRANSACTION_COLUMN_PAYMENT_TIME, dateTime[1]);
            cv.put(TRANSACTION_COLUMN_ORDER_NUMBER, newTransaction.getOrderNumber());
            cv.put(TRANSACTION_COLUMN_STATUS, newTransaction.getTransactionStatus());
            cv.put(TRANSACTION_COLUMN_AMOUNT, newTransaction.getTransactionTotal());
            cv.put(TRANSACTION_COLUMN_PAYMENT_METHOD, newTransaction.getPaymentMethod());

            long result = db.insertOrThrow(TRANSACTION_TABLE, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseHelper", "Transaction Added Successfully!");
            }
        } catch (SQLiteException e) {
            Log.e("MyDatabaseHelper", "Failed to add transaction: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if a transaction ID already exists in the "transactions" table.
     *
     * @param db            The SQLiteDatabase instance.
     * @param transactionId The transaction ID to check for existence.
     * @return true if the transaction ID exists in the "transactions" table, false otherwise.
     */
    private boolean doesTransactionIdExist(SQLiteDatabase db, String transactionId) {
        Cursor cursor = null;

        try {
            String query = "SELECT 1 FROM " + TRANSACTION_TABLE +
                    " WHERE " + TRANSACTION_COLUMN_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{transactionId});
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Retrieves user data from the provided database and populates an ArrayList with Users objects.
     *
     * @param myDB The instance of MyDatabaseHelper to access the database.
     * @param users_for_display The ArrayList to populate with Users objects.
     * @param recyclerView The RecyclerView to display the populated data.
     * @param noUserImage The ImageView to display when there are no users available.
     * @param noUserText The TextView to display when there are no users available.
     * @param resources The Resources object to retrieve app resources.
     */
    protected static void getUsers(MyDatabaseHelper myDB, ArrayList<Users> users_for_display,
                         RecyclerView recyclerView, ImageView noUserImage,
                         TextView noUserText, Resources resources){
        Cursor cursor = myDB.readAllData(USERS_TABLE_NAME);

        if (cursor.getCount() == 0){
            // If no users are found, hide the RecyclerView and show the no user message
            recyclerView.setVisibility(View.GONE);
            noUserImage.setVisibility(View.VISIBLE);
            noUserText.setVisibility(View.VISIBLE);

        } else {
            // If users are found, populate the ArrayList with user data
            while (cursor.moveToNext()){
                noUserImage.setVisibility(View.GONE);
                noUserText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                byte[] imageData = cursor.getBlob(12);

                // Convert the image byte array to a Bitmap
                Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable = new BitmapDrawable(resources, itemImageBitmap);

                Users newUser = new Users(
                        itemImageDrawable,
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(13)
                );

                users_for_display.add(newUser);
            }
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
    protected static void getItems(MyDatabaseHelper myDB, ArrayList<Items> items_for_display,
                                   GridView itemGridview, ImageView noDataImage,
                                   TextView noDataText, Resources resources) {
        // Get a cursor to the item data in the database
        Cursor cursor = myDB.readAllData(ITEMS_TABLE);

        // Check if the database is empty
        if (cursor.getCount() == 0){
            // If the database is empty, hide the item grid view and show the no data message
            itemGridview.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);

        } else{
            // If the database is not empty, populate the arrays with the item data
            while (cursor.moveToNext()){
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

    /**
     * Retrieves a list of order items for the specified order ID.
     *
     * @param orderId The order ID.
     * @param resources The resources object.
     * @return An ArrayList of order items.
     */
    public ArrayList<Items> getOrderItems(long orderId, Resources resources) {
        // Create an empty list to store the order items.
        ArrayList<Items> orderItemsList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase(); // Get the database.

        String query = "SELECT i.*, oi." + ORDER_ITEM_COLUMN_QUANTITY +
                " FROM " + ORDER_ITEMS_TABLE_NAME + " oi" +
                " INNER JOIN " + ITEMS_TABLE + " i" +
                " ON oi." + ORDER_ITEM_COLUMN_ITEM_ID + " = i." + ITEMS_COLUMN_ID +
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
                Drawable itemImageDrawable =  byteArrayToDrawable(itemImage, resources);

                Items items = new Items(itemId, itemImageDrawable, itemName, itemPrice, quantity);
                orderItemsList.add(items);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database.
        cursor.close();
        db.close();

        return orderItemsList;
    }


    /**
     * Checks if a given email already exists in the "users" table of the database.
     *
     * @param db    The SQLiteDatabase object to perform the query on.
     * @param email The email address to check for existence in the database.
     * @return {@code true} if the email exists in the "users" table, {@code false} otherwise.
     */
    private boolean emailExists(SQLiteDatabase db, String email) {
        // Perform a database query to check for the existence of the email
        Cursor cursor = db.query(USERS_TABLE_NAME, new String[]{USERS_COLUMN_EMAIL},
                USERS_COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0; // Check email exists in the database

        cursor.close(); // Close the cursor to release resources
        return exists; // Return the result indicating whether the email exists or not
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
        onCreate(db);
    }

    // TODO: Add a method to retrieve a single order from the database
    Cursor readAllOrdersData(long orderNumber){
        String query = "SELECT * FROM " + ORDERS_TABLE_NAME + " WHERE ORDER_COLUMN_ID = " + orderNumber;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /**
     * Retrieves all data from the specified database table.
     *
     * @param TABLE_NAME The name of the table from which to retrieve data.
     * @return A Cursor object containing the result set of the query, or null if an error occurs.
     */
    Cursor readAllData(String TABLE_NAME){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /**
     * Retrieves order data from the provided database and populates an ArrayList with Orders objects.
     *
     * @param myDB The instance of MyDatabaseHelper to access the database.
     * @param ordersArrayList The ArrayList to populate with Orders objects.
     * @param recyclerView The RecyclerView to display the populated data.
     * @param noDataImage The ImageView to display when there is no data available.
     * @param noDataText The TextView to display when there is no data available.
     * @param resources The Resources object to retrieve app resources.
     */
    protected static void getOrders(MyDatabaseHelper myDB, ArrayList<Orders> ordersArrayList,
                                    RecyclerView recyclerView, ImageView noDataImage, TextView noDataText,
                                    Resources resources) {
        // Get a cursor to the order data in the database
        Cursor cursor = myDB.readAllData(ORDERS_TABLE_NAME);

        // Check if the database is empty
        if (cursor.getCount() == 0){
            // If the database is empty, hide the item grid view and show the no data message
            recyclerView.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);

        } else {
            // If the database is not empty, populate the ArrayList with order data
            while (cursor.moveToNext()) {
                long orderNumber = cursor.getLong(0);

                ArrayList<Items> selectedItemsArrayList = myDB.getOrderItems(orderNumber, resources);

                int totalItems = 0 ;

                // Find the total number of items in the order
                for(Items item : selectedItemsArrayList){
                    totalItems += item.getFrequency();
                }

                Orders order = new Orders(
                        orderNumber, // Order Number
                        cursor.getString(2), // Order Date
                        cursor.getString(3), // Order Time
                        cursor.getString(5), // Order Status
                        totalItems,                     // Total Items
                        cursor.getDouble(4), // Total Amount
                        selectedItemsArrayList // Selected Items
                );

                ordersArrayList.add(order); // Add the order to the ArrayList
            }

            // Show the item grid view and hide the no data message
            recyclerView.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        }
    }

    /**
     * Retrieves details of orders with a specific order number from a database and populates
     * them into an ArrayList of Orders objects. This method queries the provided database
     * for orders matching the given orderNumber. The retrieved order details are encapsulated
     * into an Orders object, and all matching orders are added to the provided ArrayList.
     *
     * @param myDB The MyDatabaseHelper instance responsible for database operations.
     * @param ordersArrayList The ArrayList where the retrieved order details will be stored.
     * @param resources A Resources object used for retrieving localized strings, if needed.
     * @param orderNumber The specific order number for which details are to be retrieved.
     */
    protected static void getOrdersDetails(MyDatabaseHelper myDB, ArrayList<Orders> ordersArrayList,
                                    Resources resources, long orderNumber) {
        // Get a cursor to the order data in the database
        Cursor cursor = myDB.readAllData(ORDERS_TABLE_NAME);

        while (cursor.moveToNext()) {
            long currentOrderNumber = cursor.getLong(0);

            if (currentOrderNumber == orderNumber) {
                ArrayList<Items> selectedItemsArrayList = myDB.getOrderItems(orderNumber, resources);

                int totalItems = 0 ;

                // Find the total number of items in the order
                for(Items item : selectedItemsArrayList){
                    totalItems += item.getFrequency();
                }

                Orders order = new Orders(
                        orderNumber, // Order Number
                        cursor.getString(2), // Order Date
                        cursor.getString(3), // Order Time
                        cursor.getString(5), // Order Status
                        totalItems,                     // Total Items
                        cursor.getDouble(4), // Total Amount
                        selectedItemsArrayList // Selected Items
                );

                ordersArrayList.add(order); // Add the order to the ArrayList
            }
        }

    }


    /**
     * Retrieves transaction data from the provided database and populates an ArrayList with Transactions objects.
     *
     * @param myDB The instance of MyDatabaseHelper to access the database.
     * @param transactions_for_display The ArrayList to populate with Transactions objects.
     * @param recyclerView The RecyclerView to display the populated data.
     * @param noDataImage The ImageView to display when there is no data available.
     * @param noDataText The TextView to display when there is no data available.
     */
    protected static void getTransactions(MyDatabaseHelper myDB, ArrayList<Transactions> transactions_for_display,
                                          RecyclerView recyclerView, ImageView noDataImage, TextView noDataText) {
        // Get a cursor to the order data in the database
        Cursor cursor = myDB.readAllData(TRANSACTION_TABLE);

        // Check if the database is empty
        if (cursor.getCount() == 0){
            // If the database is empty, hide the item grid view and show the no data message
            recyclerView.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);

        } else {
            // If the database is not empty, populate the ArrayList with order data
            while (cursor.moveToNext()) {
                Transactions transaction = new Transactions(
                        cursor.getString(2), // Transaction Date
                        cursor.getString(3), // Transaction Time
                        cursor.getLong(1), // Order Number
                        cursor.getString(0), // Transaction ID
                        cursor.getString(5), // Transaction Status
                        cursor.getDouble(4), // Transaction Total
                        cursor.getInt(6) // Payment Method
                );

                transactions_for_display.add(transaction); // Add the order to the ArrayList
            }
            // Show the item grid view and hide the no data message
            recyclerView.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        }
    }

    public void updateItemData(String row_id, byte[] imageData, String name, double price,
                               String category, String sku, String unitType, int stock,
                               double wsPrice, double tax, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEMS_COLUMN_IMAGE, imageData);
        cv.put(ITEMS_COLUMN_NAME, name);
        cv.put(ITEMS_COLUMN_PRICE, price);
        cv.put(ITEMS_COLUMN_CATEGORY_ID, category);
        cv.put(ITEMS_COLUMN_SKU, sku);
        cv.put(ITEMS_COLUMN_TYPE, unitType);
        cv.put(ITEMS_COLUMN_STOCK_QUANTITY, stock);
        cv.put(ITEMS_COLUMN_WS_PRICE, wsPrice);
        cv.put(ITEMS_COLUMN_TAX, tax);
        cv.put(ITEMS_COLUMN_DESCRIPTION, description);

        long result = db.update(ITEMS_TABLE, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    void deleteItemRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(ITEMS_TABLE, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ITEMS_TABLE);
    }

}
