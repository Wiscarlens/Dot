package com.module.dot.data.local;

import static com.module.dot.utils.PasswordUtils.hashPassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.module.dot.model.User;

import java.util.ArrayList;

public class UserDatabase extends MyDatabaseManager {
    // TODO:  organize the name in alphabetical order
    private static final String NAME_TABLE_USERS = "users";
    private static final String LOCAL_ID_COLUMN_USERS = "local_id";
    private static final String GLOBAL_ID_COLUMN_USERS = "global_id";
    private static final String CREATOR_ID_COLUMN_USERS = "creator_id";
    private static final String FIRST_NAME_COLUMN_USERS = "first_name";
    private static final String LAST_NAME_COLUMN_USERS = "last_name";
    private static final String DOB_COLUMN_USERS = "date_of_birth";
    private static final String EMAIL_COLUMN_USERS = "email";
    private static final String PHONE_NUMBER_COLUMN_USERS = "phone_number";
    private static final String ADDRESS_COLUMN_USERS = "street_name";
    private static final String PROFILE_IMAGE_PATH_COLUMN_USERS = "profile_image_path";
    private static final String COMPANY_NAME_COLUMN_USERS = "company_name";
    private static final String POSITION_TITLE_COLUMN_USERS = "position_title"; // TODO: Foreign key to a future table call position title
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
                LOCAL_ID_COLUMN_USERS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GLOBAL_ID_COLUMN_USERS + " INTEGER, " +
                CREATOR_ID_COLUMN_USERS + " INTEGER, " +
                FIRST_NAME_COLUMN_USERS + " TEXT NOT NULL, " +
                LAST_NAME_COLUMN_USERS + " TEXT NOT NULL, " +
                DOB_COLUMN_USERS + " DATE, " +
                EMAIL_COLUMN_USERS + " TEXT NOT NULL UNIQUE, " +
                PHONE_NUMBER_COLUMN_USERS + " TEXT, " +
                ADDRESS_COLUMN_USERS + " TEXT, " +
                PROFILE_IMAGE_PATH_COLUMN_USERS + " TEXT, " +
                COMPANY_NAME_COLUMN_USERS + " TEXT," +
                POSITION_TITLE_COLUMN_USERS + " TEXT, " +
                PASSWORD_HASH_COLUMN_USERS + " TEXT NOT NULL, " +
                DATE_REGISTERED_COLUMN_USERS + " TIME DEFAULT CURRENT_TIMESTAMP);";

        db.execSQL(query_users);

        Log.i("UserDatabase", "Creating users table...");
    }


    public void createUser(User newUser) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            // TODO: This block that check email will be independent from the database
            // Check if the email already exists in the database
            if (isValueExists(db, NAME_TABLE_USERS, EMAIL_COLUMN_USERS, newUser.getEmail())) {
                Log.i("MyDatabaseManager", "Email already exists.");
                return;
            }

            ContentValues cv = new ContentValues();

            cv.put(GLOBAL_ID_COLUMN_USERS, newUser.getGlobalID());
            cv.put(CREATOR_ID_COLUMN_USERS, newUser.getCreatorID());
            cv.put(FIRST_NAME_COLUMN_USERS, newUser.getFirstName());
            cv.put(LAST_NAME_COLUMN_USERS, newUser.getLastName());
            cv.put(DOB_COLUMN_USERS, newUser.getDateOfBirth());
            cv.put(EMAIL_COLUMN_USERS, newUser.getEmail());
            cv.put(PHONE_NUMBER_COLUMN_USERS, newUser.getPhoneNumber());
            cv.put(ADDRESS_COLUMN_USERS, newUser.getAddress());
            cv.put(PROFILE_IMAGE_PATH_COLUMN_USERS, newUser.getProfileImagePath());
            cv.put(POSITION_TITLE_COLUMN_USERS, newUser.getPositionTitle());
            cv.put(COMPANY_NAME_COLUMN_USERS, newUser.getCompanyName());
            cv.put(PASSWORD_HASH_COLUMN_USERS, hashPassword(newUser.getPassword_hash()));

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


    public void readUser(ArrayList<User> userList) {
        Cursor cursor = super.readAllData(NAME_TABLE_USERS);

        try {
            // If users are found, populate the ArrayList with user data
            while (cursor.moveToNext()) {
                User newUser = new User(
                        cursor.getString(0),    // local_id
                        cursor.getString(1),    // global_id
                        cursor.getString(2),    // creator_id
                        cursor.getString(3),    // first_name
                        cursor.getString(4),    // last_name
                        cursor.getString(5),    // date_of_birth
                        cursor.getString(6),    // email
                        cursor.getString(7),    // phone_number
                        cursor.getString(8),    // address
                        cursor.getString(9),    // profile_image_path
                        cursor.getString(10),   // company_name
                        cursor.getString(11),   // position_title
                        cursor.getString(12),   // password_hash
                        cursor.getString(13)    // date_registered
                );

                userList.add(newUser);
            }
        } finally {
            // Ensure the Cursor is closed to free up resources
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
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


}
