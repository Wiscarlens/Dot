package com.example.chezelisma;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FloatingActionButton addUser;
    private FragmentActivity fragmentActivity;
    private RecyclerView recyclerView;
    private ArrayList<String> fullname = new ArrayList<>();
    private ArrayList<String> position = new ArrayList<>();
    private ArrayList<Integer> image = new ArrayList<>();
    private UserRecyclerAdapter adapter;


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

        addUser = view.findViewById(R.id.addUserButton); // Add User button

        recyclerView = view.findViewById(R.id.userList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fullname.add("Wiscarlens Lucius");
        fullname.add("Jean-Marc Elisma");
        fullname.add("Carline Clerveau");

        position.add("Admin");
        position.add("Manager");
        position.add("Associate");

        image.add(R.drawable.wiscarlens);
        image.add(R.drawable.elisma);
        image.add(R.drawable.carline);

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
}