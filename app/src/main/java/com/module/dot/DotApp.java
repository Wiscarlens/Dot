package com.module.dot;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DotApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // TODO: Create my database tables here
    }
}
