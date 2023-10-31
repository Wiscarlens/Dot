package com.module.dot.Activities.Items;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.module.dot.Database.MyDatabaseHelper;
import com.module.dot.R;

import java.util.ArrayList;


public class ItemsFragment extends Fragment {
    private FragmentActivity fragmentActivity;

    // Hold data from the database
    private final ArrayList<Items> items_for_display = new ArrayList<>();

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

        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext()); // Local database

        // Save item data from database to the arraylist
        MyDatabaseHelper.getItems(myDB, items_for_display, itemGridview, noData, getResources());

        // Initialize adapter with the arrays
        ItemGridAdapter adapter = new ItemGridAdapter(items_for_display);

        itemGridview.setAdapter(adapter);

        itemGridview.setOnItemClickListener((parent, view12, position, id) -> Toast.makeText(getContext(), "You selected " + items_for_display.get(position).getName(), Toast.LENGTH_SHORT).show());

        addItem.setOnClickListener(view1 -> {
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            New_Item_Fragment new_item_fragment = new New_Item_Fragment();

            fragmentTransaction.replace(R.id.fragment_container, new_item_fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }
}