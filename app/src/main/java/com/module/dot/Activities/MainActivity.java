package com.module.dot.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.module.dot.Activities.Home.HomeFragment;
import com.module.dot.Activities.Items.ItemsFragment;
import com.module.dot.Activities.Orders.OrdersFragment;
import com.module.dot.Activities.Settings.SettingsFragment;
import com.module.dot.Activities.Transactions.TransactionsFragment;
import com.module.dot.Activities.Users.User;
import com.module.dot.Activities.Users.UsersFragment;
import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.Database.Local.ItemDatabase;
import com.module.dot.Database.Local.OrderDatabase;
import com.module.dot.Database.Local.OrderItemsDatabase;
import com.module.dot.Database.Local.UserDatabase;
import com.module.dot.Helpers.FileManager;
import com.module.dot.Helpers.Utils;
import com.module.dot.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Intent loginActivity;

    private FirebaseAuth mAuth;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    View navigationHeader;

    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        createTables(); // Load data from the database
        loadData(); // Load data from the database

        // Find views in the navigation header
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationHeader = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);

        CircleImageView navHeaderImage = navigationHeader.findViewById(R.id.iv_profile_image);
        TextView navHeaderInitial = navigationHeader.findViewById(R.id.tv_initials);
        TextView navHeaderFullName = navigationHeader.findViewById(R.id.tv_fullName);
        TextView navHeaderEmail = navigationHeader.findViewById(R.id.tv_email);

        String UID = FirebaseHandler.getCurrentUserOnlineID(mAuth);
        String imagePath = "Profiles/" + UID;
        StorageReference storageRef = storage.getReference(imagePath);

        final long ONE_MEGABYTE = 512 * 512;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Log.i("Firebase", "Successfully retrieved profile image");
            Drawable profileImage = Utils.byteArrayToDrawable(bytes, getResources());
            navHeaderInitial.setVisibility(View.GONE);
            navHeaderImage.setImageDrawable(profileImage);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.e("Firebase", "Error getting profile image", exception);

        });

        // TODO: set navigation header here
//        navHeaderImage.setImageDrawable();

//        navHeaderFullName.setText("fullName");
//        navHeaderInitial.setText("X");
//        navHeaderEmail.setText("email");



        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Set Home fragment as default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();

            navigationView.setCheckedItem(R.id.nav_home);
        }


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if user is an administrator
                    if(!Objects.equals(dataSnapshot.child("positionTitle").getValue(String.class), "Administrator")){
                        navigationView.getMenu().findItem(R.id.nav_users).setVisible(false);
                    }

                    if (dataSnapshot.exists()) {
                        currentUser = new User(
                                dataSnapshot.child("globalID").getValue(String.class),
                                dataSnapshot.child("creatorID").getValue(String.class),
                                dataSnapshot.child("firstName").getValue(String.class),
                                dataSnapshot.child("lastName").getValue(String.class),
                                dataSnapshot.child("email").getValue(String.class),
                                dataSnapshot.child("companyName").getValue(String.class),
                                dataSnapshot.child("positionTitle").getValue(String.class),
                                dataSnapshot.child("profileImagePath").getValue(String.class)
                        );

                        // TODO: will be a above on a separate module

                        toolbar.setTitle(currentUser.getCompanyName()); // Company Name as the title of the toolbar

                        // Set the navigation header information
                        navHeaderFullName.setText(dataSnapshot.child("fullName").getValue(String.class));
                        navHeaderInitial.setText(currentUser.getFirstName());
                        navHeaderEmail.setText(currentUser.getEmail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error getting user data", databaseError.toException());
                }
            });
        }


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       final int home = R.id.nav_home;
       final int users = R.id.nav_users;
       final int items = R.id.nav_items;
       final int orders = R.id.nav_orders;
       final int transactions = R.id.nav_transactions;
       final int settings = R.id.nav_settings;
       final int logout = R.id.nav_logout;

        switch (item.getItemId()) {
            case home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                break;
            case items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ItemsFragment()).commit();
                break;
            case orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
                break;
            case transactions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransactionsFragment()).commit();
                break;
            case settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case logout:
                // Confirmation Message for log out
                showDialogMessage();

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
                    // When user click on NO
                    dialog.cancel();
                })
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    // When user click on YES
                    mAuth.signOut(); // Sign out from Server

                    // TODO: Make this block code independent from the firebase
                    // TODO: Delete all tables and images from the local database
                    // Delete Image folder from local storage
                    // Clean user local database
//                    try(UserDatabase userDatabase = new UserDatabase(this)){
//                        userDatabase.deleteAllUsers();
//                    } catch (Exception e){
//                        Log.e("MainActivity", "Error deleting all users", e);
//                    }

                    FileManager.clearAppCache(this); // Clear the app cache (local storage

                    loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                    finish();

                    // Toast Message for log out
                    String messages = getResources().getString(R.string.logout);

                    Toast.makeText(this, messages + "!", Toast.LENGTH_SHORT).show();



                }).show();


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

        try (OrderDatabase orderDatabase = new OrderDatabase(this)){
            if (!orderDatabase.isTableExists("order_items")) {
                orderDatabase.getWritableDatabase(); // Create the database
            }
        }

        try (OrderItemsDatabase orderItemsDatabase = new OrderItemsDatabase(this)){
            if (!orderItemsDatabase.isTableExists("order_items")) {
                orderItemsDatabase.onCreate(orderItemsDatabase.getWritableDatabase()); // Create the database
            }
        }

    }

    private void loadData(){
        FirebaseHandler.readItem("items", this);
        FirebaseHandler.readUser( "users", this);
        FirebaseHandler.readOrder("orders", this);
    }

}