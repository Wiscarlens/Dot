package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 August 2023.
 */

import static com.example.chezelisma.PasswordUtils.hashPassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;



import androidx.annotation.Nullable;

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
    private static final String USERS_COLUMN_PASSWORD = "password";


    // Items Table
    private static final String ITEMS_TABLE = "items";
    private static final String ITEMS_COLUMN_ID = "_id";
    private static final String ITEMS_COLUMN_IMAGE = "image";
    private static final String ITEMS_COLUMN_NAME = "name";
    private static final String ITEMS_COLUMN_PRICE = "price";
    private static final String ITEMS_COLUMN_CATEGORY = "category";
    private static final String ITEMS_COLUMN_SKU = "sku";
    private static final String ITEMS_COLUMN_TYPE = "unit_type";
    private static final String ITEMS_COLUMN_STOCK = "stock";
    private static final String ITEMS_COLUMN_WS_PRICE = "wholesales_price";
    private static final String ITEMS_COLUMN_TAX = "tax";
    private static final String ITEMS_COLUMN_DESCRIPTION = "description";
    private static final String ITEMS_COLUMN_CREATED_DATE = "create_date";

    // Order Table
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_CREATOR_ID = "order_creator";
    private static final String ORDER_COLUMN_ORDER_DATE = "order_date";
    private static final String ORDER_COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String ORDER_COLUMN_PAYMENT_METHOD = "payment_method";
    private static final String ORDER_COLUMN_PAYMENT_STATUS = "payment_status";

    // Transaction Table
    private static final String TRANSACTION_TABLE = "transactions";
    private static final String TRANSACTION_COLUMN_ID = "_id";
    private static final String TRANSACTION_COLUMN_ORDER_ID = "order_id";
    private static final String TRANSACTION_COLUMN_PAYMENT_DATE = "payment_date";
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
                USERS_COLUMN_PASSWORD + " TEXT NOT NULL);";

        // SQL query to create the "items" table
        String query_items = "CREATE TABLE " + ITEMS_TABLE +
                " (" + ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEMS_COLUMN_IMAGE + " BLOB, " +
                ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                ITEMS_COLUMN_PRICE + " REAL NOT NULL, " +
                ITEMS_COLUMN_CATEGORY + " TEXT, " +
                ITEMS_COLUMN_SKU + " TEXT, " +
                ITEMS_COLUMN_TYPE + " TEXT, " +
                ITEMS_COLUMN_STOCK + " INTEGER, " +
                ITEMS_COLUMN_WS_PRICE + " REAL, " +
                ITEMS_COLUMN_TAX + " REAL, " +
                ITEMS_COLUMN_DESCRIPTION + " TEXT, " +
                ITEMS_COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

        // SQL query to create the "orders" table
        String query_orders = "CREATE TABLE " + ORDERS_TABLE_NAME +
                " (" + ORDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_COLUMN_CREATOR_ID + " INTEGER NOT NULL, " +
                ORDER_COLUMN_ORDER_DATE + " DATE NOT NULL, " +
                ORDER_COLUMN_TOTAL_AMOUNT + " REAL NOT NULL, " +
                ORDER_COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, " +
                ORDER_COLUMN_PAYMENT_STATUS + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ORDER_COLUMN_CREATOR_ID
                + ") REFERENCES " + USERS_TABLE_NAME + " (" + USERS_COLUMN_ID + "));";


        // SQL query to create the "transactions" table
        String query_transactions = "CREATE TABLE " + TRANSACTION_TABLE +
                " (" + TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRANSACTION_COLUMN_ORDER_ID + " INTEGER NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_DATE + " DATE NOT NULL, " +
                TRANSACTION_COLUMN_AMOUNT + " REAL NOT NULL, " +
                TRANSACTION_COLUMN_STATUS + " TEXT NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_METHOD + " TEXT, " +
                " FOREIGN KEY (" + TRANSACTION_COLUMN_ORDER_ID +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "));";

        db.execSQL(query_users);
        db.execSQL(query_items);
        db.execSQL(query_orders);
        db.execSQL(query_transactions);
    }

    /**
     * Adds an item to the "items" table in the database with the provided information.
     *
     * @param imageData   The byte array representing the image data of the item.
     * @param name        The name of the item.
     * @param price       The price of the item.
     * @param category    The category of the item.
     * @param sku         The SKU (stock keeping unit) of the item.
     * @param unitType    The unit type of the item (e.g., "pcs", "kg", "lb").
     * @param stock       The available stock quantity of the item.
     * @param wsPrice     The wholesale price of the item.
     * @param tax         The tax rate applied to the item.
     * @param description A description of the item.
     *
     * @throws SQLiteException if there is an error while accessing the database or inserting the item.
     */
    public void addItem(byte[] imageData, String name, double price, String category, String sku, String unitType,
                        int stock, double wsPrice, double tax, String description) throws SQLiteException {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(ITEMS_COLUMN_IMAGE, imageData);
            cv.put(ITEMS_COLUMN_NAME, name);
            cv.put(ITEMS_COLUMN_PRICE, price);
            cv.put(ITEMS_COLUMN_CATEGORY, category);
            cv.put(ITEMS_COLUMN_SKU, sku);
            cv.put(ITEMS_COLUMN_TYPE, unitType);
            cv.put(ITEMS_COLUMN_STOCK, stock);
            cv.put(ITEMS_COLUMN_WS_PRICE, wsPrice);
            cv.put(ITEMS_COLUMN_TAX, tax);
            cv.put(ITEMS_COLUMN_DESCRIPTION, description);

            long result;

            result = db.insertOrThrow(ITEMS_TABLE, null, cv);
            if (result != -1) {
                Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw e;
        }
    }



    /**
     * Adds a new user to the "users" table in the database.
     *
     * @param firstName     The first name of the user.
     * @param middleName    The middle name of the user (can be null if not available).
     * @param lastName      The last name of the user.
     * @param dob           The date of birth of the user (in format "yyyy-MM-dd") or null if not available.
     * @param gender        The gender of the user (e.g., "Male", "Female", "Other") or null if not available.
     * @param email         The email address of the user (must be unique and not null).
     * @param phoneNumber   The phone number of the user or null if not available.
     * @param streetName    The street name/address of the user or null if not available.
     * @param city          The city of the user or null if not available.
     * @param state         The state/province of the user or null if not available.
     * @param zipCode       The zip code or postal code of the user or null if not available.
     * @param profileImage  The profile image data as a byte array or null if not available.
     * @param position      The position or job title of the user or null if not available.
     * @param password      The password of the user (must not be null).
     *
     * @throws SQLiteException if there is an error while accessing the database or inserting the user.
     */
    public void addUser(String firstName, String middleName, String lastName, String dob, String gender,
                        String email, String phoneNumber, String streetName, String city, String state,
                        int zipCode, byte[] profileImage, String position, String password)
            throws SQLiteException {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the email already exists in the database
            if (emailExists(db, email)) {
                Toast.makeText(context, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues cv = new ContentValues();

            // Hash the plain text password using bcrypt
            String hashedPassword = hashPassword(password);

            cv.put(USERS_COLUMN_FIRST_NAME, firstName);
            cv.put(USERS_COLUMN_MIDDLE_NAME, middleName);
            cv.put(USERS_COLUMN_LAST_NAME, lastName);
            cv.put(USERS_COLUMN_DOB, dob);
            cv.put(USERS_COLUMN_GENDER, gender);
            cv.put(USERS_COLUMN_EMAIL, email);
            cv.put(USERS_COLUMN_PHONE_NUMBER, phoneNumber);
            cv.put(USERS_COLUMN_STREET_NAME, streetName);
            cv.put(USERS_COLUMN_CITY, city);
            cv.put(USERS_COLUMN_STATE, state);
            cv.put(USERS_COLUMN_ZIP_CODE, zipCode);
            cv.put(USERS_COLUMN_PROFILE_IMAGE, profileImage);
            cv.put(USERS_COLUMN_POSITION, position);
            cv.put(USERS_COLUMN_PASSWORD, hashedPassword); // Add the hashed password to database

            long result = db.insertOrThrow(USERS_TABLE_NAME, null, cv);

            if (result != -1) {
                Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            e.printStackTrace(); // Log the error stack trace for debugging
            Toast.makeText(context, "Failed to add user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw e;
        }
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
                USERS_COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        boolean exists = cursor.getCount() > 0; // Check email exists in the database

        cursor.close(); // Close the cursor to release resources
        return exists; // Return the result indicating whether the email exists or not
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
        onCreate(db);
    }

    Cursor readAllItemsData(){
        String query = "SELECT * FROM " + ITEMS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readAllUsersData(){
        String query = "SELECT * FROM " + USERS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateItemData(String row_id, byte[] imageData, String name, double price, String category, String sku, String unitType,
                        int stock, double wsPrice, double tax, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEMS_COLUMN_IMAGE, imageData);
        cv.put(ITEMS_COLUMN_NAME, name);
        cv.put(ITEMS_COLUMN_PRICE, price);
        cv.put(ITEMS_COLUMN_CATEGORY, category);
        cv.put(ITEMS_COLUMN_SKU, sku);
        cv.put(ITEMS_COLUMN_TYPE, unitType);
        cv.put(ITEMS_COLUMN_STOCK, stock);
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
