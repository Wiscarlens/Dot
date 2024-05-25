package com.module.dot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    public static void saveImageLocally(Context context, Drawable drawable, String folderName, String fileName) {
        if (!isFolderExists(folderName)){
            createFolder(folderName);
        }

        File directory = context.getDir(folderName, Context.MODE_PRIVATE);
        File file = new File(directory, fileName + ".png");

        // Convert the drawable to a Bitmap
        Bitmap bitmap = Utils.drawableToBitmap(drawable);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            Log.i("FileManager", "Successfully saved image locally: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("FileManager", "Error saving image locally", e);
        }
    }

    public static Drawable loadImageLocally(Context context, String folder, String fileName) {
        File directory = context.getDir(folder, Context.MODE_PRIVATE);
        File file = new File(directory, fileName + ".png");

        if (file.exists()) {
            Bitmap bitmap = null;
            try (FileInputStream fis = new FileInputStream(file)) {
                bitmap = BitmapFactory.decodeStream(fis);
            } catch (IOException e) {
                Log.e("FileManager", "Error loading image locally", e);
            }
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            // If the file does not exist, return null or provide a default Drawable
            return null;
        }
    }

    public static void clearAppCache(Context context) {
        try {
            deleteFolder(context, "Items");
            deleteFolder(context, "Profiles");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteFolder(Context context, String folderName) {
        File directory = context.getDir(folderName, Context.MODE_PRIVATE);

        // Check if the directory exists
        if (directory.exists()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            // Delete each file in the directory
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }

            // Delete the directory itself
            if (directory.delete()) {
                Log.i("FileManager", "Successfully deleted folder: " + directory.getAbsolutePath());
            } else {
                Log.e("FileManager", "Error deleting folder: " + directory.getAbsolutePath());
            }
        } else {
            Log.i("FileManager", "Folder does not exist: " + directory.getAbsolutePath());
        }
    }



    public static boolean isFolderExists(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    // Create a folder
    public static void createFolder(String path) {
        File file = new File(path);
        file.mkdirs();
    }



}
