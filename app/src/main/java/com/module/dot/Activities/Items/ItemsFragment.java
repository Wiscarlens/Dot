package com.module.dot.Activities.Items;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.module.dot.Activities.MainActivity;
import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.Database.Local.ItemDatabase;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;


public class ItemsFragment extends Fragment {
    private FragmentActivity fragmentActivity;

    // Hold data from the database
    private final ArrayList<Item> item_for_display = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout noData = view.findViewById(R.id.noDataItemFragmentLL); // When Database is empty
        GridView itemGridview = view.findViewById(R.id.itemList); // When list of item will show
        FloatingActionButton addItem = view.findViewById(R.id.addButton); // Add Item floating button

        FirebaseHandler.readItem("items", getContext());

        try (ItemDatabase itemDatabase = new ItemDatabase(getContext())) {
            if (itemDatabase.isTableEmpty("items")) {
                itemDatabase.showEmptyStateMessage(itemGridview, noData);
            } else {
                itemDatabase.showStateMessage(itemGridview, noData);

                itemDatabase.readItem(item_for_display); // Read data from database and save it the arraylist
            }
        } catch (Exception e) {
            Log.i("UserFragment", Objects.requireNonNull(e.getMessage()));
        }

        // Initialize adapter with the arrays
        ItemGridAdapter adapter = new ItemGridAdapter(item_for_display, getContext());

        itemGridview.setAdapter(adapter);

        itemGridview.setOnItemClickListener((parent, view12, position, id) -> Toast.makeText(getContext(), "You selected " + item_for_display.get(position).getName(), Toast.LENGTH_SHORT).show());

        if(!Objects.equals(MainActivity.currentUser.getPositionTitle(), "Administrator")){
            addItem.setVisibility(View.GONE);
        }

        addItem.setOnClickListener(view1 -> {
            showFragment(new NewItemFragment());
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}