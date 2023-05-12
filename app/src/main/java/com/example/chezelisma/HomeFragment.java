package com.example.chezelisma;

/********************************************
 *Created by Wiscarlens Lucius on 1 February 2023.*
 ********************************************/

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private GridView itemGridview;
    private FloatingActionButton scanButton;
    private Button chargeButton;

    private ArrayList<Integer> image = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemPrice = new ArrayList<>();
    private ArrayList<String> itemUnitType = new ArrayList<>();
    private ArrayList<Integer> backgroundColor = new ArrayList<>();
    private ArrayList<String> selectedItems = new ArrayList<>();
    private ItemGridAdapter adapter;
    private BottomSheetAdapter bottomSheetAdapter;

    // Variable to test bottom sheet
    private RecyclerView recyclerView;
    private ArrayList<String> productName =  new ArrayList<>();
    private ArrayList<Integer> productTotal  = new ArrayList<>();
    private ArrayList<String> productPrice  = new ArrayList<>();


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemGridview = view.findViewById(R.id.itemList);
        scanButton = view.findViewById(R.id.scanButton);
        chargeButton = view.findViewById(R.id.Charge);

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
        itemPrice.add("25.00");
        itemPrice.add("30.00");
        itemPrice.add("50.00");

        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Unit");
        itemUnitType.add("Hourly");
        itemUnitType.add("Hourly");
        itemUnitType.add("Hourly");

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


        AtomicReference<Double> price = new AtomicReference<>(0.0);

        // When user select an item
        itemGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "You selected " + itemName.get(position), Toast.LENGTH_SHORT).show();
                Double itemSelected = Double.parseDouble(itemPrice.get(position));

                // Test bottom Sheet
                productName.add(itemName.get(position));
                productTotal.add(2);
                productPrice.add(itemPrice.get(position));

                bottomSheetAdapter = new BottomSheetAdapter(productName, productTotal, productPrice, getContext());



                // Add selected item price together
                price.set(price.get() + itemSelected);

                // Current total charge
                String currentCharge = "$ " + String.format("%.2f", price.get());

                // Set the button text to the current value of price
                chargeButton.setText(currentCharge);

            }
        });

        // When user click on charge button
        chargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open bottom sheet layout
                showButtonDialog();
            }
        });

    }

    public void showButtonDialog(){
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transaction_design_bottom_sheet);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}