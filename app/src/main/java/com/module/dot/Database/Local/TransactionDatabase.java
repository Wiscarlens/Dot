package com.module.dot.Database.Local;

import static com.module.dot.Helpers.LocalFormat.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.module.dot.Activities.Transactions.Transaction;
import com.module.dot.Helpers.Utils;

import java.util.ArrayList;

public class TransactionDatabase extends MyDatabaseManager {
    private static final String TRANSACTION_TABLE_NAME = "transactions";
    private  static final String TRANSACTION_COLUMN_GLOBAL_ID = "global_id";
    private static final String TRANSACTION_COLUMN_CREATOR_ID = "creator_id";
    private static final String TRANSACTION_COLUMN_ORDER_NUMBER = "order_id";
    private static final String TRANSACTION_COLUMN_PAYMENT_DATE = "payment_date";
    private static final String TRANSACTION_COLUMN_PAYMENT_TIME = "payment_time";
    private static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    private static final String TRANSACTION_COLUMN_STATUS = "status";
    private static final String TRANSACTION_COLUMN_PAYMENT_METHOD = "payment_method";

    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_ID = "_id";
    public TransactionDatabase(@Nullable Context context) {
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
        // SQL query to create the "transactions" table
        String query_transactions = "CREATE TABLE " + TRANSACTION_TABLE_NAME +
                " (" + TRANSACTION_COLUMN_GLOBAL_ID + " TEXT PRIMARY KEY, " +
                TRANSACTION_COLUMN_CREATOR_ID + " TEXT NOT NULL, " +
                TRANSACTION_COLUMN_ORDER_NUMBER + " INTEGER NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_DATE + " DATE, " +
                TRANSACTION_COLUMN_PAYMENT_TIME + " TIME, " +
                TRANSACTION_COLUMN_AMOUNT + " REAL NOT NULL, " +
                TRANSACTION_COLUMN_STATUS + " TEXT NOT NULL, " +
                TRANSACTION_COLUMN_PAYMENT_METHOD + " TEXT, " +
                " FOREIGN KEY (" + TRANSACTION_COLUMN_ORDER_NUMBER +
                ") REFERENCES " + ORDERS_TABLE_NAME + " (" + ORDER_COLUMN_ID + "));";

        db.execSQL(query_transactions);

        Log.i("TransactionDatabase", "Creating transaction table...");
    }

    /**
     * Inserts a new transaction record into the database.
     *
     * @param newTransaction The transaction object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void createTransaction(Transaction newTransaction)
            throws SQLiteException {
        {
            try (SQLiteDatabase db = this.getWritableDatabase()) {
                // TODO: This block that check email will be independent from the database
                // Check if the email already exists in the database
                if (isValueExists(db, TRANSACTION_TABLE_NAME, TRANSACTION_COLUMN_GLOBAL_ID, newTransaction.getGlobalID())) {
                    return;
                }

                ContentValues cv = new ContentValues();

                String[] dateTime = getCurrentDateTime(); // Get the current date and time

                cv.put(TRANSACTION_COLUMN_GLOBAL_ID, newTransaction.getGlobalID());
                cv.put(TRANSACTION_COLUMN_CREATOR_ID, newTransaction.getCreatorID());
                cv.put(TRANSACTION_COLUMN_PAYMENT_DATE, dateTime[0]);
                cv.put(TRANSACTION_COLUMN_PAYMENT_TIME, dateTime[1]);
                cv.put(TRANSACTION_COLUMN_ORDER_NUMBER, newTransaction.getOrderNumber());
                cv.put(TRANSACTION_COLUMN_STATUS, newTransaction.getTransactionStatus());
                cv.put(TRANSACTION_COLUMN_AMOUNT, newTransaction.getTransactionTotal());
                cv.put(TRANSACTION_COLUMN_PAYMENT_METHOD, newTransaction.getPaymentMethod());

                long result = db.insertOrThrow(TRANSACTION_TABLE_NAME, null, cv);

                if (result != -1) {
                    Log.i("MyDatabaseManager", "Transaction Added Successfully!");
                }
            } catch (SQLiteException e) {
                Log.e("MyDatabaseManager", "Failed to add transaction: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Retrieves transaction data from the provided database and populates an ArrayList with Transactions objects.
     *
     * @param transaction_for_display The ArrayList to populate with Transactions objects.
     */
    public void readTransaction(ArrayList<Transaction> transaction_for_display) {
            // Get a cursor to the order data in the database
            Cursor cursor = super.readAllData(TRANSACTION_TABLE_NAME);

            try {
                while (cursor.moveToNext()) {
                    Transaction transaction = new Transaction(
                            cursor.getString(0), // Global ID
                            cursor.getLong(1), // Order Number
                            cursor.getString(2), // Transaction Date
                            cursor.getString(3), // Transaction Time
                            cursor.getString(5), // Transaction Status
                            cursor.getDouble(4), // Transaction Total Amount
                            cursor.getString(6) // Payment Method
                    );

                    transaction_for_display.add(transaction); // Add the order to the ArrayList
                }
            } finally {
                // Ensure the Cursor is closed to free up resources
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }



        }

        public void updateTransaction () {

        }

        public void deleteTransaction () {

        }



}
