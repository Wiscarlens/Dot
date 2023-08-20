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
    protected static void storeItemsDataInArrays(MyDatabaseHelper myDB, ArrayList<Items> items_for_display,
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
                        cursor.getLong(0),    // id
                        cursor.getString(2),    // name
                        itemImageDrawable,                 // imageData
                        cursor.getDouble(3),    // price
                        cursor.getString(6),    // unitType
                        // TODO: Default Background Color
                        R.color.white                      // backgroundColor
                );

                items_for_display.add(item); // Add the item to the ArrayList

                // TODO: to be remove
                //itemId_and_name.put(cursor.getInt(0), cursor.getString(2));

            }

            // Show the item grid view and hide the no data message
            itemGridview.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        }
    }
}
