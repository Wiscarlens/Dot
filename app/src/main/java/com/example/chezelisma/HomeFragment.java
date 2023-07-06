package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private Button chargeButton;
    private ArrayList<Integer> image = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemPrice = new ArrayList<>();
    private ArrayList<String> itemUnitType = new ArrayList<>();
    private ArrayList<Integer> backgroundColor = new ArrayList<>();
    private ArrayList<String> selectProductName =  new ArrayList<>();
    private ArrayList<Double> selectProductPrice = new ArrayList<>();

    // Select item total
    private AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
    private String currentCharge;

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

        GridView itemGridview = view.findViewById(R.id.itemList);
        FloatingActionButton scanButton = view.findViewById(R.id.scanButton);
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

        ItemGridAdapter adapter = new ItemGridAdapter(image, itemName, itemPrice, itemUnitType, backgroundColor, getContext());

        itemGridview.setAdapter(adapter);

        // When user select an item
        itemGridview.setOnItemClickListener((parent, view1, position, id) -> {
            Double itemSelected = Double.parseDouble(itemPrice.get(position));

            // Add selected item price together
            totalPrice.set(totalPrice.get() + itemSelected);

            // Format the double value into currency format
            currentCharge = CurrencyFormat.getCurrencyFormat(totalPrice.get());

            // Set the button text to the current value of price
            chargeButton.setText(currentCharge);

            // Add data to the bottom sheet adapter
            selectProductName.add(itemName.get(position));
            selectProductPrice.add(Double.valueOf(itemPrice.get(position)));

        });

        // When user click on charge button
        chargeButton.setOnClickListener(v -> {
            // Open bottom sheet layout
            Dialog dialog = showButtonDialog();
            final double bottomSheetHeight = 0.56; // Initialize to 56% of the screen height
            setBottomSheetHeight(dialog, bottomSheetHeight);
        });

        //When user click on scanner button
        scanButton.setOnClickListener(v -> scanCode());
    }

    public Dialog showButtonDialog(){
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        TextView transactionTotal = dialog.findViewById(R.id.transactionTotal);
        Button checkoutButton = dialog.findViewById(R.id.checkoutButton);
        // Variable to test bottom sheet
        RecyclerView bottomSheetRecyclerView = dialog.findViewById(R.id.transactionSheetList); // Find the RecyclerView in the layout

        transactionTotal.setText(currentCharge);

        // Bottom sheet recycle view
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create the adapter and set it to the RecyclerView
        BottomSheetAdapter bottomSheetAdapter = new BottomSheetAdapter(selectProductName, selectProductPrice, getContext());
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);

        checkoutButton.setOnClickListener(v -> {
            dialog.dismiss();

            // Check if recycle view is empty before check out
            if (!selectProductName.isEmpty()) {
                // Sending data to another fragment
                Bundle result = new Bundle();
                result.putString("price", currentCharge);
                getParentFragmentManager().setFragmentResult("priceData", result);

                // Open confirmation fragment
                showDialogMessage();

            } else {
                String message = getResources().getString(R.string.empty_cart);
                Toast.makeText(fragmentActivity, message, Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }

    private void setBottomSheetHeight(Dialog dialog, double heightPercentage){
        // Set the height of the dialog
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (heightPercentage * getScreenHeight()));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    // Set bottom sheet height
    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    // Scanner bar/QR code
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Press Volume Up to Turn Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        scannerLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions>  scannerLauncher = registerForActivityResult(new ScanContract(), result -> {
        String res = result.getContents();
        if(res.equals("096619756803")){
            Toast.makeText(fragmentActivity, "Water added", Toast.LENGTH_SHORT).show();
            //Test scanner check out
            // Add selected item price together
            //price.set(price.get() + 1.99);
        } else{
            Toast.makeText(fragmentActivity, "Item not found", Toast.LENGTH_SHORT).show();
        }
    });

    private void showDialogMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getResources().getString(R.string.confirm))
                .setMessage(getResources().getString(R.string.confirm_checkout))
                .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                    // If user click on NO nothing happen
                })
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    // Open ConfirmationFragment when user click on YES
                    FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                    fragmentTransaction.replace(R.id.fragment_container, confirmationFragment);
                    fragmentTransaction.commit();
                }).show();
    }



}