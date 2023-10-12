package com.module.dot.Database.Local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class OrderDatabase extends MyDatabaseHelper {
    // Order Table
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDER_COLUMN_ID = "_id";
    private static final String ORDER_COLUMN_CREATOR_ID = "order_creator";
    private static final String ORDER_COLUMN_DATE = "order_date";
    private static final String ORDER_COLUMN_TIME = "order_time";
    private static final String ORDER_COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String ORDER_COLUMN_STATUS = "order_status";

    // Order Items Table
    private static final String ORDER_ITEMS_TABLE_NAME = "order_items";
    private static final String ORDER_ITEM_COLUMN_ID = "_id";
    private static final String ORDER_ITEM_COLUMN_ORDER_ID = "order_id";
    private static final String ORDER_ITEM_COLUMN_ITEM_ID = "item_id";
    private static final String ORDER_ITEM_COLUMN_QUANTITY = "item_quantity";

    public OrderDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void createOrder(){

    }

    public void readOrder(){

    }

    public void updateOrder(){

    }

    public void deleteOrder(){

    }
}
