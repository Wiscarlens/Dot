package com.example.chezelisma;


import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Utility class containing methods that are use in different classes.
 */
public class Utils {
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
        Cursor cursor = myDB.readAllItemsData();

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
        Cursor cursor = myDB.readAllOrdersData();

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
        Cursor cursor = myDB.readAllData("transactions");

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
                        cursor.getString(1), // Order Number
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

    /**
     * Converts a byte array to a Drawable.
     *
     * @param imageData The image data in byte array format.
     * @param resources The resources object.
     * @return The Drawable object.
     */
    public static Drawable byteArrayToDrawable(byte[] imageData, Resources resources) {
        // Convert the image byte array to a Bitmap
        Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        // Convert the Bitmap to a Drawable if needed
        return new BitmapDrawable(resources, itemImageBitmap);
    }

    /**
     * Converts a Drawable object to a byte array.
     *
     * @param drawable The Drawable object to convert.
     * @return The byte array representation of the Drawable object.
     */
    public static byte[] getByteArrayFromDrawable(Drawable drawable) {
        // Get the bitmap from the drawable.
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Create a byte array output stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the bitmap to PNG format and write it to the output stream.
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        // Get the byte array from the output stream.
        return outputStream.toByteArray();
    }
}
