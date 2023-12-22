package com.module.dot.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.module.dot.Activities.Users.Users;
import com.module.dot.Activities.Users.UsersFragment;
import com.module.dot.Database.Cloud.FirebaseHandler;
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

    public static Users currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
                        currentUser = new Users(
                                null, // Profile Image Path
                                dataSnapshot.child("firstName").getValue(String.class),
                                dataSnapshot.child("lastName").getValue(String.class),
                                dataSnapshot.child("email").getValue(String.class)
                        );

                        // TODO: will be a above on a separate module
                        // Company Name
                        toolbar.setTitle(dataSnapshot.child("companyName").getValue(String.class));

                        // Set the navigation header information
                        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
                        navHeaderFullName.setText(fullName);
                        navHeaderInitial.setText(currentUser.getFirstName());
                        navHeaderEmail.setText(dataSnapshot.child("email").getValue(String.class));
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

                mAuth.signOut();

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

                    // Toast Message for log out
                    String messages = getResources().getString(R.string.logout);

                    Toast.makeText(this, messages + "!", Toast.LENGTH_SHORT).show();

                }).show();

    }

}