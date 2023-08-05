package com.example.chezelisma;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FloatingActionButton addUser;
    private FragmentActivity fragmentActivity;
    private ImageView noUserImage;
    private TextView noUserText;
    private RecyclerView recyclerView;
    private ArrayList<String> fullname = new ArrayList<>();
    private ArrayList<String> position = new ArrayList<>();
    private ArrayList<Drawable> image = new ArrayList<>();
    private UserRecyclerAdapter adapter;

    private MyDatabaseHelper myDB;


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

        noUserImage = view.findViewById(R.id.no_user_imageview); // When users Database is empty
        noUserText = view.findViewById(R.id.no_user_textview); // When users Database is empty
        recyclerView = view.findViewById(R.id.userList);
        addUser = view.findViewById(R.id.addUserButton); // Add User button

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        fullname.add("Wiscarlens Lucius");
//        fullname.add("Jean-Marc Elisma");
//        fullname.add("Carline Clerveau");
//
//        position.add("Admin");
//        position.add("Manager");
//        position.add("Associate");
//
//        image.add(R.drawable.wiscarlens);
//        image.add(R.drawable.elisma);
//        image.add(R.drawable.carline);

        myDB = new MyDatabaseHelper(getContext()); // Local database

        storeItemsDataInArrays(); // Save item data from database to the arraylist

        adapter = new UserRecyclerAdapter(fullname, position, image, getContext());

        recyclerView.setAdapter(adapter);


        // Create new user button
        addUser.setOnClickListener(view1 -> {
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SignUpFragment signUpActivity = new SignUpFragment();
            fragmentTransaction.replace(R.id.fragment_container, signUpActivity);
            fragmentTransaction.commit();
        });
    }

    private void storeItemsDataInArrays(){
        Cursor cursor = myDB.readAllUsersData();

        if (cursor.getCount() == 0){
            recyclerView.setVisibility(View.GONE);
            noUserImage.setVisibility(View.VISIBLE);
            noUserText.setVisibility(View.VISIBLE);

        } else {
            // Retrieve item data from the database
            while (cursor.moveToNext()){
                noUserImage.setVisibility(View.GONE);
                noUserText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                byte[] imageData = cursor.getBlob(12);

                // Convert the image byte array to a Bitmap
                Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable = new BitmapDrawable(getResources(), itemImageBitmap);

                // Concatenate first and last name
                String FullName = cursor.getString(1) + " " + cursor.getString(3);

                fullname.add(FullName);
                position.add(cursor.getString(13));
                image.add(itemImageDrawable);
            }
        }
    }
}