package com.module.dot.Database.Local;

import static com.module.dot.Helpers.PasswordUtils.hashPassword;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.Activities.Users.Users;
import com.module.dot.Helpers.Utils;

import java.util.ArrayList;

public class UserDatabase extends MyDatabaseHelper {
    private static final String USERS_TABLE_NAME = "users";
    private static final String USERS_COLUMN_ID = "user_id";
    private static final String USERS_COLUMN_FIRST_NAME = "first_name";
    private static final String USERS_COLUMN_MIDDLE_NAME = "middle_name";
    private static final String USERS_COLUMN_LAST_NAME = "last_name";
    private static final String USERS_COLUMN_DOB = "date_of_birth";
    private static final String USERS_COLUMN_GENDER = "gender";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String USERS_COLUMN_STREET_NAME = "street_name";
    private static final String USERS_COLUMN_CITY = "city";
    private static final String USERS_COLUMN_STATE = "state";
    private static final String USERS_COLUMN_ZIP_CODE = "zip_code";
    private static final String USERS_COLUMN_PROFILE_IMAGE = "profile_picture";
    private static final String USERS_COLUMN_POSITION = "position";
    private static final String USERS_COLUMN_PASSWORD = "password_hash";
    private static final String USERS_COLUMN_DATE_REGISTERED = "date_registered";

    public UserDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the "users" table
        String query_users = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                USERS_COLUMN_MIDDLE_NAME + " TEXT, " +
                USERS_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                USERS_COLUMN_DOB + " DATE, " +
                USERS_COLUMN_GENDER + " TEXT, " +
                USERS_COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                USERS_COLUMN_PHONE_NUMBER + " TEXT, " +
                USERS_COLUMN_STREET_NAME + " TEXT, " +
                USERS_COLUMN_CITY + " TEXT, " +
                USERS_COLUMN_STATE + " TEXT, " +
                USERS_COLUMN_ZIP_CODE + " TEXT, " +
                USERS_COLUMN_PROFILE_IMAGE + " BLOB, " +
                USERS_COLUMN_POSITION + " TEXT, " +
                USERS_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                USERS_COLUMN_DATE_REGISTERED + " TIME DEFAULT CURRENT_TIMESTAMP);";

        db.execSQL(query_users);
    }

    public void createUser(Users newUsers) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the email already exists in the database
            if (emailExists(db, newUsers.getEmail())) {
                Log.i("MyDatabaseHelper", "Email already exists.");

                Toast.makeText(context, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();

                return;
            }

            ContentValues cv = new ContentValues();

            cv.put(USERS_COLUMN_FIRST_NAME, newUsers.getFirstName());
            cv.put(USERS_COLUMN_MIDDLE_NAME, newUsers.getMiddleName());
            cv.put(USERS_COLUMN_LAST_NAME, newUsers.getLastName());
            cv.put(USERS_COLUMN_DOB, newUsers.getDateOfBirth());
            cv.put(USERS_COLUMN_GENDER, newUsers.getGender());
            cv.put(USERS_COLUMN_EMAIL, newUsers.getEmail());
            cv.put(USERS_COLUMN_PHONE_NUMBER, newUsers.getPhoneNumber());
            cv.put(USERS_COLUMN_STREET_NAME, newUsers.getStreetName());
            cv.put(USERS_COLUMN_CITY, newUsers.getCity());
            cv.put(USERS_COLUMN_STATE, newUsers.getState());
            cv.put(USERS_COLUMN_ZIP_CODE, newUsers.getZipCode());
            cv.put(USERS_COLUMN_PROFILE_IMAGE, Utils.getByteArrayFromDrawable(newUsers.getProfileImage()));
            cv.put(USERS_COLUMN_POSITION, newUsers.getPosition());
            cv.put(USERS_COLUMN_PASSWORD, hashPassword(newUsers.getPassword()));

            long result = db.insertOrThrow(USERS_TABLE_NAME, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseHelper", "User Added Successfully!");
            }
        } catch (SQLiteException e) {
            e.printStackTrace(); // Log the error stack trace for debugging
            Log.e("MyDatabaseHelper", "Failed to add user: " + e.getMessage());
            throw e;
        }
    }


    // TODO: Clean this code, remove all unnecessary UI code
    public void readUser(com.module.dot.Database.MyDatabaseHelper myDB, ArrayList<Users> users_for_display,
                     RecyclerView recyclerView, ImageView noUserImage,
                     TextView noUserText, Resources resources) {
        Cursor cursor = super.readAllData(USERS_TABLE_NAME);

        if (cursor.getCount() == 0){
            // If no users are found, hide the RecyclerView and show the no user message
            recyclerView.setVisibility(View.GONE);
            noUserImage.setVisibility(View.VISIBLE);
            noUserText.setVisibility(View.VISIBLE);

        } else {
            // If users are found, populate the ArrayList with user data
            while (cursor.moveToNext()){
                noUserImage.setVisibility(View.GONE);
                noUserText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                byte[] imageData = cursor.getBlob(12);

                // Convert the image byte array to a Bitmap
                Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable = new BitmapDrawable(resources, itemImageBitmap);

                Users newUser = new Users(
                        itemImageDrawable,
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(13)
                );

                users_for_display.add(newUser);
            }
        }


    }

    public void updateUser() {

    }

    public void deleteUser(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(USERS_TABLE_NAME, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO:  This method can be merge with Transaction method in TransactionDatabase.java
    /**
     * Checks if a given email already exists in the "users" table of the database.
     *
     * @param db    The SQLiteDatabase object to perform the query on.
     * @param email The email address to check for existence in the database.
     * @return {@code true} if the email exists in the "users" table, {@code false} otherwise.
     */
    private boolean emailExists(SQLiteDatabase db, String email) {
        // Perform a database query to check for the existence of the email
        Cursor cursor = db.query(USERS_TABLE_NAME, new String[]{USERS_COLUMN_EMAIL},
                USERS_COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0; // Check email exists in the database

        cursor.close(); // Close the cursor to release resources
        return exists; // Return the result indicating whether the email exists or not
    }
}
