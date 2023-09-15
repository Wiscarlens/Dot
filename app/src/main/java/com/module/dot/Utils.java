package com.module.dot;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;


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

    /**
     * Formats an integer as a string with at least 5 digits by adding leading zeros if necessary.
     *
     * @param num The integer to be formatted.
     * @return A string representation of the integer with at least 5 digits, including leading zeros.
     */
    public static String formatOrderNumber(long num) {
        // Convert the integer to a string
        String numStr = Long.toString(num);

        // Calculate the number of leading zeros needed
        int numZeros = Math.max(0, 5 - numStr.length());

        // Add the leading zeros to the string
        StringBuilder formattedNum = new StringBuilder();
        for (int i = 0; i < numZeros; i++) {
            formattedNum.append('0');
        }
        formattedNum.append(numStr);

        return formattedNum.toString();
    }

    /**
     * Generates a random 16-character transaction number consisting of uppercase letters (A-Z) and digits (0-9).
     *
     * @return A randomly generated 16-character transaction number.
     */

    public static String generateTransactionNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder transactionNumber = new StringBuilder();

        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int TRANSACTION_NUMBER_LENGTH = 20;

        for (int i = 0; i < TRANSACTION_NUMBER_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            transactionNumber.append(randomChar);
        }

        return transactionNumber.toString();
    }


}
