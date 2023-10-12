package com.module.dot.Database.Local;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class MyDatabaseHelper extends SQLiteOpenHelper {
    protected final Context context;
    private static final String DATABASE_NAME = "Module.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public abstract void onCreate(SQLiteDatabase db);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Retrieves all data from the specified database table.
     *
     * @param TABLE_NAME The name of the table from which to retrieve data.
     * @return A Cursor object containing the result set of the query, or null if an error occurs.
     */
    Cursor readAllData(String TABLE_NAME){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
