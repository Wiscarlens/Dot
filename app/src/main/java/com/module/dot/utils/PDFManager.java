package com.module.dot.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.OutputStream;

public class PDFManager {

    /**
     * Creates a PDF document from a given View and saves it to the Downloads directory.
     *
     * @param orderNumberString The order number used to generate the PDF file name.
     * @param receiptView              The View to be rendered as a PDF document.
     * @param receiptFooterLayout     The LinearLayout containing the View to be rendered as a PDF.
     * @param context           The Context object for accessing resources and system services.
     */
    public static void createPDF(String orderNumberString, View receiptView, LinearLayout receiptFooterLayout, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create a ContentValues object to store the file's metadata
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "Receipt_" + orderNumberString + ".pdf");

            // Get the content resolver and insert the file into the Downloads directory
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            try {
                // Open an output stream using the document's URI
                assert uri != null;
                OutputStream outputStream = contentResolver.openOutputStream(uri);

                if (outputStream != null) {
                    // Define the page width and height in points (1/72 inch)
                    int pageWidth = 1280; // 8.5 inches (8.5 * 72 points)
                    int pageHeight = 1920; // 11 inches (11 * 72 points)

                    // Create the PDF document
                    PdfDocument document = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();

                    // Hide the receipt footer before drawing the PDF
                    receiptFooterLayout.setVisibility(View.GONE);

                    // Measure and draw the updated receiptView onto the PDF canvas
                    receiptView.measure(View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY));

                    receiptView.layout(0, 0, pageWidth, pageHeight);
                    receiptView.draw(canvas);

                    document.finishPage(page);

                    // Write the PDF content to the output stream
                    document.writeTo(outputStream);

                    // Close the output stream and the PDF document
                    outputStream.close();
                    document.close();

                    Log.i("PDF", "PDF Created Successfully!");

                    // Show the receipt footer after drawing the PDF
                    receiptFooterLayout.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle versions prior to Android 10 if needed
        }
    }
}
