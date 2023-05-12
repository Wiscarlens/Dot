package com.example.chezelisma;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ItemsFragment extends Fragment {
    FloatingActionButton addItem;
    private FragmentActivity fragmentActivity;

    private GridView itemGridview;

    private ArrayList<Integer> image = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemPrice = new ArrayList<>();
    private ArrayList<String> itemUnitType = new ArrayList<>();
    private ArrayList<Integer> backgroundColor = new ArrayList<>();

    private ItemGridAdapter adapter;

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

        // Add item in the arraylist
        image.add(R.drawable.prestige);
        image.add(R.drawable.cokecane);
        image.add(R.drawable.jumex);
        image.add(R.drawable.toro);
        image.add(R.drawable.sevenup);
        image.add(R.drawable.barbancourt);
        image.add(R.drawable.mortalcombat);
        image.add(R.drawable.nba);
        image.add(R.drawable.fifa);

        itemName.add("Prestige");
        itemName.add("Coca Cola");
        itemName.add("Jumex");
        itemName.add("Toro");
        itemName.add("7 UP");
        itemName.add("Barbancourt");
        itemName.add("Mortal Combat");
        itemName.add("NBA 2023");
        itemName.add("FIFA 2023");

        itemPrice.add("5.99");
        itemPrice.add("1.99");
        itemPrice.add("3.47");
        itemPrice.add("6.99");
        itemPrice.add("1.89");
        itemPrice.add("15.75");
        itemPrice.add("25");
        itemPrice.add("30");
        itemPrice.add("50");

        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Hour");
        itemUnitType.add("Hour");
        itemUnitType.add("Hour");

        backgroundColor.add(R.color.white);
        backgroundColor.add(R.color.pink_gold);
        backgroundColor.add(R.color.blue_AF);
        backgroundColor.add(R.color.white);
        backgroundColor.add(R.color.pink_gold);
        backgroundColor.add(R.color.blue_AF);
        backgroundColor.add(R.color.white);
        backgroundColor.add(R.color.pink_gold);
        backgroundColor.add(R.color.blue_AF);

        adapter = new ItemGridAdapter(image, itemName, itemPrice, itemUnitType, backgroundColor, getContext());

        itemGridview.setAdapter(adapter);

        itemGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), "You selected " + itemName.get(position), Toast.LENGTH_SHORT).show();

            }
        });

        // Add Item button
        addItem = view.findViewById(R.id.addButton);

        addItem.setOnClickListener(view1 -> {
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            New_Item_Fragment new_item_fragment = new New_Item_Fragment();
            fragmentTransaction.add(R.id.fragment_container, new_item_fragment);
            fragmentTransaction.commit();
        });
    }
}