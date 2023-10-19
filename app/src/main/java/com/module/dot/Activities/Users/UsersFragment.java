package com.module.dot.Activities.Users;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.module.dot.Database.MyDatabaseHelper;
import com.module.dot.R;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private final ArrayList<Users> users_for_display = new ArrayList<>();

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout noUser = view.findViewById(R.id.noUserFragmentLL); // When users Database is empty
//        ImageView noUserImage = view.findViewById(R.id.no_user_imageview); // When users Database is empty
//        TextView noUserText = view.findViewById(R.id.no_user_textview); // When users Database is empty
        RecyclerView recyclerView = view.findViewById(R.id.userList);
        FloatingActionButton addUser = view.findViewById(R.id.addUserButton); // Add User button

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext()); // Local database

        // Save item data from database to the arraylist
        MyDatabaseHelper.getUsers(
                myDB, // Local database
                users_for_display, // ArrayList to store Users objects for display
                recyclerView, // RecyclerView UI element to display users
                noUser, // Linear Layout UI element to show when no data is available
//                noUserText, // TextView UI element to show when no data is available
                getResources() // Resources instance to access app resources
        );

        UserRecyclerAdapter adapter = new UserRecyclerAdapter(users_for_display, getContext());

        recyclerView.setAdapter(adapter);

        // Create new user button
        addUser.setOnClickListener(view1 -> {
            // Create new fragment manager and transaction
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace current fragment with SignupFragment
            SignupFragment signUpActivity = new SignupFragment();
            fragmentTransaction.replace(R.id.fragment_container, signUpActivity);
            fragmentTransaction.commit();
        });
    }

}