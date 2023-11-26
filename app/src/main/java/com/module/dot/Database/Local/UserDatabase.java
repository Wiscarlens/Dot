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

public class UserDatabase extends MyDatabaseManager {
    // TODO:  organize the name in alphabetical order
    private static final String NAME_TABLE_USERS = "users";
    private static final String ID_COLUMN_USERS = "user_id";
    private static final String FIRST_NAME_COLUMN_USERS = "first_name";
    private static final String MIDDLE_NAME_COLUMN_USERS = "middle_name";
    private static final String LAST_NAME_COLUMN_USERS = "last_name";
    private static final String DOB_COLUMN_USERS = "date_of_birth";
    private static final String GENDER_COLUMN_USERS = "gender";
    private static final String EMAIL_COLUMN_USERS = "email";
    private static final String PHONE_NUMBER_COLUMN_USERS = "phone_number";
    private static final String STREET_NAME_COLUMN_USERS = "street_name";
    private static final String CITY_COLUMN_USERS = "city";
    private static final String STATE_COLUMN_USERS = "state";
    private static final String ZIP_CODE_COLUMN_USERS = "zip_code";
    private static final String PROFILE_PICTURE_COLUMN_USERS = "profile_picture";
    private static final String POSITION_TITLE_COLUMN_USERS = "position"; // TODO: Rename to "position_title" Foreign key to a future table call position
    private static final String PASSWORD_HASH_COLUMN_USERS = "password_hash";
    private static final String DATE_REGISTERED_COLUMN_USERS = "date_registered";

    public UserDatabase(@Nullable Context context) {
        super(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing "users" table
        db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE_USERS);

        // Create a new "users" table
        onCreate(db);
    }

    protected void createTable(SQLiteDatabase db){
        // SQL query to create the "users" table
        String query_users = "CREATE TABLE " + NAME_TABLE_USERS + " (" +
                ID_COLUMN_USERS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIRST_NAME_COLUMN_USERS + " TEXT NOT NULL, " +
                MIDDLE_NAME_COLUMN_USERS + " TEXT, " +
                LAST_NAME_COLUMN_USERS + " TEXT NOT NULL, " +
                DOB_COLUMN_USERS + " DATE, " +
                GENDER_COLUMN_USERS + " TEXT, " +
                EMAIL_COLUMN_USERS + " TEXT NOT NULL UNIQUE, " +
                PHONE_NUMBER_COLUMN_USERS + " TEXT, " +
                STREET_NAME_COLUMN_USERS + " TEXT, " +
                CITY_COLUMN_USERS + " TEXT, " +
                STATE_COLUMN_USERS + " TEXT, " +
                ZIP_CODE_COLUMN_USERS + " TEXT, " +
                PROFILE_PICTURE_COLUMN_USERS + " BLOB, " +
                POSITION_TITLE_COLUMN_USERS + " TEXT, " +
                PASSWORD_HASH_COLUMN_USERS + " TEXT NOT NULL, " +
                DATE_REGISTERED_COLUMN_USERS + " TIME DEFAULT CURRENT_TIMESTAMP);";

        db.execSQL(query_users);

        Log.i("UserDatabase", "Creating users table...");
    }


    public void createUser(Users newUsers) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Check if the email already exists in the database
            if (super.isEmailExists(db, newUsers.getEmail(), NAME_TABLE_USERS, EMAIL_COLUMN_USERS)) {
                Log.i("MyDatabaseManager", "Email already exists.");

                Toast.makeText(context, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();

                return;
            }

            ContentValues cv = new ContentValues();

            cv.put(FIRST_NAME_COLUMN_USERS, newUsers.getFirstName());
            cv.put(MIDDLE_NAME_COLUMN_USERS, newUsers.getMiddleName());
            cv.put(LAST_NAME_COLUMN_USERS, newUsers.getLastName());
            cv.put(DOB_COLUMN_USERS, newUsers.getDateOfBirth());
            cv.put(GENDER_COLUMN_USERS, newUsers.getGender());
            cv.put(EMAIL_COLUMN_USERS, newUsers.getEmail());
            cv.put(PHONE_NUMBER_COLUMN_USERS, newUsers.getPhoneNumber());
            cv.put(STREET_NAME_COLUMN_USERS, newUsers.getStreetName());
            cv.put(CITY_COLUMN_USERS, newUsers.getCity());
            cv.put(STATE_COLUMN_USERS, newUsers.getState());
            cv.put(ZIP_CODE_COLUMN_USERS, newUsers.getZipCode());
            cv.put(PROFILE_PICTURE_COLUMN_USERS, Utils.getByteArrayFromDrawable(newUsers.getProfileImage()));
            cv.put(POSITION_TITLE_COLUMN_USERS, newUsers.getPosition());
            cv.put(PASSWORD_HASH_COLUMN_USERS, hashPassword(newUsers.getPassword()));

            long result = db.insertOrThrow(NAME_TABLE_USERS, null, cv);

            if (result != -1) {
                Log.i("MyDatabaseManager", "User Added Successfully!");
            }
        } catch (SQLiteException e) {
            e.printStackTrace(); // Log the error stack trace for debugging
            Log.e("MyDatabaseManager", "Failed to add user: " + e.getMessage());
            throw e;
        }
    }


    public void readUser(ArrayList<Users> users_for_display) {
        Cursor cursor = super.readAllData(NAME_TABLE_USERS);

        // If users are found, populate the ArrayList with user data
        while (cursor.moveToNext()){
            // Retrieve item image as byte array
            byte[] imageData = cursor.getBlob(12);

            // Convert the Bitmap to a Drawable if needed
            Drawable itemImageDrawable = convertByteArrayToDrawable(imageData);

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

    public void updateUser() {

    }

    public void deleteUser(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(NAME_TABLE_USERS, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

//    // TODO:  This method can be merge with Transaction method in TransactionDatabase.java
//    /**
//     * Checks if a given email already exists in the "users" table of the database.
//     *
//     * @param db    The SQLiteDatabase object to perform the query on.
//     * @param email The email address to check for existence in the database.
//     * @return {@code true} if the email exists in the "users" table, {@code false} otherwise.
//     */
//    private boolean isEmailExists(SQLiteDatabase db, String email) {
//        // Perform a database query to check for the existence of the email
//        Cursor cursor = db.query(NAME_TABLE_USERS, new String[]{EMAIL_COLUMN_USERS},
//                EMAIL_COLUMN_USERS + "=?", new String[]{email},
//                null, null, null);
//
//        boolean exists = cursor.getCount() > 0; // Check email exists in the database
//
//        cursor.close(); // Close the cursor to release resources
//        return exists; // Return the result indicating whether the email exists or not
//    }
}
