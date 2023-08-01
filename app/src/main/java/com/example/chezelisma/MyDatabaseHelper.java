package com.example.chezelisma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Module.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "items";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SKU = "sku";
    private static final String COLUMN_TYPE = "unit_type";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_WS_PRICE = "wholesales_price";
    private static final String COLUMN_TAX = "tax";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CREATED_DATE = "create_date";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PRICE + " REAL NOT NULL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_SKU + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_STOCK + " INTEGER, " +
                COLUMN_WS_PRICE + " REAL, " +
                COLUMN_TAX + " REAL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";


        db.execSQL(query);
    }

    void addItem(byte[] imageData, String name, double price, String category, String sku, String unitType,
                 int stock, double wsPrice, double tax, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IMAGE, imageData);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_SKU, sku);
        cv.put(COLUMN_TYPE, unitType);
        cv.put(COLUMN_STOCK, stock);
        cv.put(COLUMN_WS_PRICE, wsPrice);
        cv.put(COLUMN_TAX, tax);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateItemData(String row_id, byte[] imageData, String name, double price, String category, String sku, String unitType,
                        int stock, double wsPrice, double tax, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IMAGE, imageData);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_SKU, sku);
        cv.put(COLUMN_TYPE, unitType);
        cv.put(COLUMN_STOCK, stock);
        cv.put(COLUMN_WS_PRICE, wsPrice);
        cv.put(COLUMN_TAX, tax);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }



}
