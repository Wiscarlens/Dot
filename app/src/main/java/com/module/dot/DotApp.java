package com.module.dot;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.module.dot.data.local.ItemDatabase;
import com.module.dot.data.local.OrderDatabase;
import com.module.dot.data.local.OrderItemsDatabase;
import com.module.dot.data.local.TransactionDatabase;
import com.module.dot.data.local.UserDatabase;
import com.module.dot.data.remote.FirebaseHandler;

public class DotApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        createTables();
        loadData();

    }


    private void createTables() {
        try (ItemDatabase itemDatabase = new ItemDatabase(this)){
            if (!itemDatabase.isTableExists("items")){
                itemDatabase.onCreate(itemDatabase.getWritableDatabase()); // Create the database
            }
        }

        try (UserDatabase userDatabase = new UserDatabase(this)){
            if (!userDatabase.isTableExists("users")){
                userDatabase.onCreate(userDatabase.getWritableDatabase()); // Create the database
            }
        }

        try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(this)){
            if (!orderItemsDatabase.isTableExists("order_items")) {
                orderItemsDatabase.onCreate(orderItemsDatabase.getWritableDatabase()); // Create the database
            }
        }

        try (TransactionDatabase transactionDatabase = new TransactionDatabase(this)){
            if (!transactionDatabase.isTableExists("transactions")) {
                transactionDatabase.onCreate(transactionDatabase.getWritableDatabase()); // Create the database
            }
        }

        try (OrderDatabase orderDatabase = new OrderDatabase(this)){
            if (!orderDatabase.isTableExists("orders")) {
                orderDatabase.onCreate(orderDatabase.getWritableDatabase()); // Create the database
            }
        }

    }

    private void loadData(){
        FirebaseHandler.readItem("items", this);
        FirebaseHandler.readUser( "users", this);
        FirebaseHandler.readOrder("orders", this);
        FirebaseHandler.readTransaction("transactions", this);
    }
}
