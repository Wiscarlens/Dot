package com.module.dot.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageStorageManager {
    public static void saveImageLocally(Context context, Drawable drawable, String folderName, String imageName) {
        // Convert the drawable to a Bitmap
        Bitmap bitmap = Utils.drawableToBitmap(drawable);

        // Save the Bitmap to local storage (you can customize the directory and file name)
        File directory = context.getDir(folderName, Context.MODE_PRIVATE);
        File file = new File(directory, imageName + ".png");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            Log.i("LocalImage", "Successfully saved image locally: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("LocalImage", "Error saving image locally", e);
        }
    }

    public static Drawable loadImageLocally(Context context, String folder, String imageName) {
        File directory = context.getDir(folder, Context.MODE_PRIVATE);
        File file = new File(directory, imageName + ".png");

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            // If the file does not exist, return null or provide a default Drawable
            return null;
        }
    }

}
