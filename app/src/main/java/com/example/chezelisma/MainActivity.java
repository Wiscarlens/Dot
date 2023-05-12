package com.example.chezelisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent loginActivity;
    String messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Set Home fragment as default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                break;
            case R.id.nav_transactions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransactionsFragment()).commit();
                break;
            case R.id.nav_items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ItemsFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                // Confirmation Message for log out
                showDialogMessage();

                // Toast Message for log out
                messages = getResources().getString(R.string.logout);
                Toast.makeText(this, messages + "!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.confirm))
                .setMessage(getResources().getString(R.string.confirm_logout))
                .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                    // If user click on NO
                })
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    // Open login Activity when user click on YES
                    loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }).show();
    }


}