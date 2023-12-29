package com.module.dot.Activities.Users;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.Database.Local.UserDatabase;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;

public class UsersFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    FirebaseAuth auth;
    private UserDatabase userDatabase;

    private LinearLayout noUser;
    private UserRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private final ArrayList<User> user_for_display = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();


    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        userDatabase = new UserDatabase(getContext());

        noUser = view.findViewById(R.id.noUserFragmentLL); // When users Database is empty
        recyclerView = view.findViewById(R.id.userList);
        FloatingActionButton addUser = view.findViewById(R.id.addUserButton); // Add User button

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if (!userDatabase.isTableExists("users")){
            userDatabase.onCreate(userDatabase.getWritableDatabase()); // Create the database

            FirebaseHandler.readUser("users", getContext());

        } else {
            // TODO: just use firebase only
            FirebaseHandler.readUser("users", getContext());
        }

        try {
            if (userDatabase.isTableEmpty("users")) {
                userDatabase.showEmptyStateMessage(recyclerView, noUser);
            } else {
                userDatabase.showStateMessage(recyclerView, noUser);
                user_for_display.clear(); // Clear the list before updating it

                userDatabase.readUser(user_for_display); // Read data from database and save it the arraylist

                adapter = new UserRecyclerAdapter(user_for_display, getContext());
                recyclerView.setAdapter(adapter);
            }

        } catch (Exception e) {
            Log.i("UserFragment", Objects.requireNonNull(e.getMessage()));
        }



//        if (userDatabase.isTableEmpty("users")) {
//            userDatabase.showEmptyStateMessage(recyclerView, noUser);
//        } else {
//            userDatabase.showStateMessage(recyclerView, noUser);
//            users_for_display.clear(); // Clear the list before updating it
//            userDatabase.readUser(users_for_display); // Read data from database and save it the arraylist
//
//            // Update the RecyclerView after the data fetch is complete
//            Log.d("UserFragmentTest", "onStart: " + users_for_display.get(0).getPositionTitle());
//        }

//        UserRecyclerAdapter adapter = new UserRecyclerAdapter(users_for_display, getContext());
//        recyclerView.setAdapter(adapter);

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

    public int refreshRecyclerView() {
        user_for_display.clear();
        userDatabase.readUser(user_for_display);
        return user_for_display.size() - 1; // return the index of the last item
    }

    public UserRecyclerAdapter getAdapter() {
        return adapter;
    }

}