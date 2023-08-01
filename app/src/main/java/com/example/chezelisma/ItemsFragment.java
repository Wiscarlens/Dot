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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ItemsFragment extends Fragment {
    private FloatingActionButton addItem;
    private FragmentActivity fragmentActivity;

    private GridView itemGridview;

    // Hold data from the database
    private ArrayList<Drawable> image;
    private ArrayList<String> itemName;
    private ArrayList<String> itemPrice;
    private ArrayList<String> itemUnitType;
    private ArrayList<Integer> backgroundColor;

    private ItemGridAdapter adapter;

    private MyDatabaseHelper myDB;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemGridview = view.findViewById(R.id.itemList);
        addItem = view.findViewById(R.id.addButton); // Add Item floating button

        // Local database
        myDB = new MyDatabaseHelper(getContext());

        image = new ArrayList<>();
        itemName = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemUnitType = new ArrayList<>();
        backgroundColor = new ArrayList<>();

        storeDataInArrays(); // Save item data from database to the arraylist

        adapter = new ItemGridAdapter(image, itemName, itemPrice, itemUnitType, backgroundColor, getContext());

        itemGridview.setAdapter(adapter);

        itemGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You selected " + itemName.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        addItem.setOnClickListener(view1 -> {
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            New_Item_Fragment new_item_fragment = new New_Item_Fragment();
            fragmentTransaction.replace(R.id.fragment_container, new_item_fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0){
            Toast.makeText(fragmentActivity, "Empty", Toast.LENGTH_SHORT).show();

        } else{
            while (cursor.moveToNext()){
                // Retrieve item data from the database
                byte[] imageData = cursor.getBlob(1);

                // Convert the image byte array to a Bitmap
                Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable = new BitmapDrawable(getResources(), itemImageBitmap);

                image.add(itemImageDrawable); // Test Line
                itemName.add(cursor.getString(2));
                itemPrice.add(cursor.getString(3));
                itemUnitType.add(cursor.getString(6));
                backgroundColor.add(R.color.white); // Default Background Color
            }
        }
    }
}