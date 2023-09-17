package com.module.dot.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.module.dot.R;

public class BarcodeManager {

    /**
     * Generates a Code 128 barcode as a Drawable based on the provided input text.
     *
     * @param inputText The text to encode in the barcode.
     * @param context   The Android Context used to access resources.
     * @return A Drawable representing the generated Code 128 barcode, or null if an error occurs.
     */
    public static Drawable generateBarcode(String inputText, Context context) {
        Drawable barcodeDrawable = null;

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    inputText,
                    BarcodeFormat.CODE_128,
                    2000,
                    300
            );

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap barcodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    barcodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ?
                            ContextCompat.getColor(context, R.color.black) :
                            ContextCompat.getColor(context, R.color.white)
                    );
                }
            }

            // Convert the Bitmap to a Drawable
            barcodeDrawable = new BitmapDrawable(context.getResources(), barcodeBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return barcodeDrawable;
    }
}
