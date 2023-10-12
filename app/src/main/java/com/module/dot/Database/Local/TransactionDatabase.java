package com.module.dot.Database.Local;

import static com.module.dot.Helpers.LocalFormat.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.Activities.Transactions.Transactions;
import com.module.dot.Helpers.Utils;

import java.util.ArrayList;

public class TransactionDatabase extends MyDatabaseHelper {
    private static final String TRANSACTION_TABLE = "transactions";
    private static final String TRANSACTION_COLUMN_ID = "_id";
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

        db.execSQL(query_transactions);

    }

    /**
     * Inserts a new transaction record into the database.
     *
     * @param newTransaction The transaction object containing information to be added.
     * @throws SQLiteException If there is an issue with the SQLite database operations.
     */
    public void createTransaction(Transactions newTransaction)
            throws SQLiteException {
        {
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
    public void readTransaction(com.module.dot.Database.MyDatabaseHelper myDB, ArrayList<Transactions> transactions_for_display,
                                RecyclerView recyclerView, ImageView noDataImage, TextView noDataText) {
            // Get a cursor to the order data in the database
            Cursor cursor = super.readAllData(TRANSACTION_TABLE);

            // Check if the database is empty
            if (cursor.getCount() == 0) {
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

        public void updateTransaction () {

        }

        public void deleteTransaction () {

        }


        // TODO: This method can be merge with emailExists() method in UserDatabase.java

        /**
         * Checks if a transaction ID already exists in the "transactions" table.
         *
         * @param db            The SQLiteDatabase instance.
         * @param transactionId The transaction ID to check for existence.
         * @return true if the transaction ID exists in the "transactions" table, false otherwise.
         */
        private boolean doesTransactionIdExist (SQLiteDatabase db, String transactionId){
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


}
