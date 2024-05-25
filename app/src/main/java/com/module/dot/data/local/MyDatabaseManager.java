package com.module.dot.data.local;

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
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public abstract class MyDatabaseManager extends SQLiteOpenHelper {
    // TODO: make class protected instead of public
    protected Context context;
    private static final String DATABASE_NAME = "Dot.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
     * Checks if a specified database table is empty.
     * This method queries the specified table using a cursor to retrieve all data.
     * It then checks the count of rows in the cursor result set and returns true
     * if the count is zero, indicating that the table is empty. Otherwise, it returns false.
     *
     * @param tableName The name of the database table to be checked for emptiness.
     * @return {@code true} if the table is empty, {@code false} otherwise.
     */
    public boolean isTableEmpty(String tableName){
        Cursor cursor = readAllData(tableName);

        return cursor.getCount() == 0;
    }

    public void showEmptyStateMessage(View view, LinearLayout noData){
        // If the database is empty, hide the item grid view and show the no data message
        view.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    public void showStateMessage(View view, LinearLayout noData) {
        // Show the item grid view and hide the no data message
        view.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    /**
     * Converts an image byte array to a Drawable.
     *
     * @param imageData The byte array containing the image data
     * @return A Drawable representation of the image
     */
    public Drawable convertByteArrayToDrawable(byte[] imageData) {
        Resources resources = context.getResources();

        // Convert the image byte array to a Bitmap
        Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        // Convert the Bitmap to a Drawable
        return new BitmapDrawable(resources, itemImageBitmap);
    }

    /**
     * Checks if a given email already exists in the "users" table of the database.
     *
     * @param db    The SQLiteDatabase object to perform the query on.
     * @param email The email address to check for existence in the database.
     * @return {@code true} if the email exists in the "users" table, {@code false} otherwise.
     */
    public boolean isEmailExists(SQLiteDatabase db, String email, String table_name, String email_column) {
        // Perform a database query to check for the existence of the email
        Cursor cursor = db.query(table_name, new String[]{email_column},
                email_column + "=?", new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0; // Check email exists in the database

        cursor.close(); // Close the cursor to release resources
        return exists; // Return the result indicating whether the email exists or not
    }

    /**
     * Checks if a given value already exists in a specified column of the database table.
     *
     * @param db          The SQLiteDatabase object to perform the query on.
     * @param tableName   The name of the table to check.
     * @param columnName  The name of the column to check for existence.
     * @param columnValue The value to check for existence in the specified column.
     * @return {@code true} if the value exists in the specified column, {@code false} otherwise.
     */
    public boolean isValueExists(SQLiteDatabase db, String tableName, String columnName, String columnValue) {
        // Perform a database query to check for the existence of the value
        Cursor cursor = db.query(tableName, new String[]{columnName},
                columnName + "=?", new String[]{columnValue},
                null, null, null);

        boolean exists = cursor.getCount() > 0; // Check if the value exists in the database

        cursor.close(); // Close the cursor to release resources
        return exists;  // Return the result indicating whether the value exists or not
    }


    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void deleteAll(String tableName) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Delete all rows from the table
            int result = db.delete(tableName, null, null);

            if (result > 0) {
                Log.i(tableName + " Table", "Successfully deleted all");
            } else {
                Log.e(tableName+ " Table", "Failed to delete");
            }
        } catch (SQLiteException e) {
            Log.e(tableName + " Table", "Error deleting: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
