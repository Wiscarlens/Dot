package com.example.chezelisma;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


import java.io.ByteArrayOutputStream;


/**
 * Utility class containing methods that are use in different classes.
 */
public class Utils {
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
