package com.module.dot.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.module.dot.R;

public class SplashActivity extends AppCompatActivity {
    // TODO: The application should not provide its own launch screen More...
    //Inspection info:Starting in Android 12 (API 31+), the application's
    // Launch Screen is provided by the system and the application should not
    // create its own, otherwise the user will see two splashscreens.
    // Please check the SplashScreen class to check how the Splash
    // Screen can be controlled and customized.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIMER = 3000;

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }, SPLASH_TIMER);
    }
}