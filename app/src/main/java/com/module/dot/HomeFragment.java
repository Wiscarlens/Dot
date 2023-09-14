package com.module.dot;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageView;
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

    private final ArrayList<Items> items_for_display = new ArrayList<>();
    private final ArrayList<Items> selectedItems =  new ArrayList<>();

    // Select item total
    private final AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
    private String currentCharge;
    private long newOrderID;

    private MyDatabaseHelper myDB;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView noDataImage = view.findViewById(R.id.no_data_imageview); // When Database is empty
        TextView noDataText = view.findViewById(R.id.no_data_textview); // When Database is empty
        GridView itemGridview = view.findViewById(R.id.itemList);
        FloatingActionButton scanButton = view.findViewById(R.id.scanButton);
        chargeButton = view.findViewById(R.id.Charge);

        // Local database
        myDB = new MyDatabaseHelper(getContext());

        // Save item data from database to the arraylist
        MyDatabaseHelper.getItems(
                myDB, // Local database
                items_for_display, // ArrayList to store Items objects for display
                itemGridview, // GridView UI element to display items
                noDataImage, // ImageView UI element to show when no data is available
                noDataText, // TextView UI element to show when no data is available
                getResources() // Resources instance to access app resources
        );

        ItemGridAdapter itemGridAdapter = new ItemGridAdapter(items_for_display);
        itemGridview.setAdapter(itemGridAdapter);

        // When user select an item
        itemGridview.setOnItemClickListener((parent, view1, position, id) -> {
            // Find the selected item
            Items selectedItem = new Items(
                    items_for_display.get(position).getId(),
                    items_for_display.get(position).getName(),
                    items_for_display.get(position).getPrice(),
                    items_for_display.get(position).getSku(),
                    1
            );

            // TODO: Optimize - All the line below can be part of addToSElected Items method
            Double itemSelectedPrice = items_for_display.get(position).getPrice();

            // Add selected item price together
            totalPrice.set(totalPrice.get() + itemSelectedPrice);

            // Format the double value into currency format
            currentCharge = LocalFormat.getCurrencyFormat(totalPrice.get());

            // Set the button text to the current value of price
            chargeButton.setText(currentCharge);

            addToSelectedItems(selectedItem);

        });

        // When user click on charge button
        chargeButton.setOnClickListener(v -> {
            // Open bottom sheet layout
            Dialog dialog = showButtonDialog();

            final double bottomSheetHeight = 0.56; // Initialize to 56% of the screen height

            setBottomSheetHeight(dialog, bottomSheetHeight);
        });

        //When user click on scanner button
        scanButton.setOnClickListener(v -> {
                if(items_for_display.isEmpty()) {
                    Toast.makeText(fragmentActivity, "No item in database", Toast.LENGTH_SHORT).show();
                } else {
                    scanCode();  // Scan barcode to add item to cart
                }
            }
        );
    }

    public Dialog showButtonDialog(){
        final Dialog bottomSheetDialog = new Dialog(getContext());

        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_layout);

        TextView transactionTotal = bottomSheetDialog.findViewById(R.id.transactionTotal);
        Button checkoutButton = bottomSheetDialog.findViewById(R.id.checkoutButton);
        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.transactionSheetList); // Find the RecyclerView in the layout

        transactionTotal.setText(currentCharge);

        // Bottom sheet recycle view
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create the adapter and set it to the RecyclerView
        BottomSheetAdapter bottomSheetAdapter = new BottomSheetAdapter(selectedItems, getContext());
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);

        checkoutButton.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // Check if recycle view is empty before check out
            if (!selectedItems.isEmpty()) {
                // Sending total price to confirmationFragment
                Bundle result = new Bundle();
                result.putString("price", currentCharge);
                getParentFragmentManager().setFragmentResult("priceData", result);

                // Confirmation message to check out
                AlertDialog.Builder checkoutConfirmation = new AlertDialog.Builder(getContext());

                checkoutConfirmation.setTitle(getResources().getString(R.string.confirm))
                        .setMessage(getResources().getString(R.string.confirm_checkout))
                        .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                             // Complete with the value 'false'
                        }).setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                           // If user click on yes
                            // Create New Order

                            Orders newOrder = new Orders(
                                    "1", // TODO: Replace with the actual user ID
                                    totalPrice.get(), // Total amount
                                    "Completed" // TODO: replace with the actual Order status
                            );

                            newOrderID = myDB.setOrder(newOrder);

                            for (Items item : selectedItems) {
                                myDB.setOrderItem(newOrderID, item.getId(), item.getFrequency());
                            }

                            // TODO: Create a new transaction
                            Transactions newTransaction = new Transactions(
                                    newOrderID, // Order ID
                                    "APPROVE", // TODO: replace with the actual transaction status
                                    totalPrice.get(),
                                    R.drawable.visa // TODO: replace with the actual payment method
                            );

                            myDB.setTransaction(newTransaction);

                            // Sending order number to receipt fragment
                            Bundle orderNumberBundle = new Bundle();
                            orderNumberBundle.putLong("orderNumber", newOrderID);
                            getParentFragmentManager().setFragmentResult("orderNumberData", orderNumberBundle);

                            // Open Confirmation fragment
                            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                            fragmentTransaction.replace(R.id.fragment_container, confirmationFragment);
                            fragmentTransaction.commit();

                        }).show();

            } else {
                String message = getResources().getString(R.string.empty_cart);
                Toast.makeText(fragmentActivity, message, Toast.LENGTH_SHORT).show();
            }

        });

        bottomSheetDialog.show();

        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        return bottomSheetDialog;
    }

    /**
     * Sets the height of a dialog displayed as a bottom sheet to a specified percentage of the screen height.
     *
     * @param dialog The dialog for which to set the height.
     * @param heightPercentage The desired height of the dialog as a percentage of the screen height.
     */
    private void setBottomSheetHeight(Dialog dialog, double heightPercentage){
        // Set the height of the dialog
        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (heightPercentage * getScreenHeight()));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    /**
     * Retrieves the height of the device screen in pixels.
     *
     * @return The height of the screen in pixels.
     */
    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * Initiates a barcode scanning process using the specified scan options.
     */
    private void scanCode(){
        ScanOptions options = new ScanOptions();

        options.setPrompt("Press Volume Up to Turn Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        scannerLauncher.launch(options);
    }

    /**
     * Result launcher for initiating barcode scanning and handling the scanning result.
     */
    ActivityResultLauncher<ScanOptions>  scannerLauncher = registerForActivityResult(new ScanContract(), result -> {
        String scanItem = result.getContents();

        for (Items item : items_for_display) {
             if(scanItem.equals(item.getSku())) {
                 addToSelectedItems(item); // Add the item to the selectedItems list
                 // Add selected item price together
                 totalPrice.set(totalPrice.get() + item.getPrice()); // Add the item price to the total price

                 // Format the double value into currency format
                 currentCharge = LocalFormat.getCurrencyFormat(totalPrice.get());

                 // Set the button text to the current value of price
                 chargeButton.setText(currentCharge);

                 Toast.makeText(fragmentActivity, item.getName() + " added", Toast.LENGTH_SHORT).show();
                 break;
             } else{
                 Toast.makeText(fragmentActivity, "Item not found", Toast.LENGTH_SHORT).show();
             }
        }
    });

    /**
     * Adds an item to the selectedItems list or increases its frequency if it already exists.
     *
     * @param newItem The item to be added or whose frequency should be increased.
     */
    private void addToSelectedItems(Items newItem) {
        // Check if the item is already in selectedItems
        for (Items item : selectedItems) {
            if (item.getId() == newItem.getId()) {
                // Item already exists, increase frequency
                item.setFrequency(item.getFrequency() + 1);
                return; // Exit the method since the item was found
            }
        }

        // Item not found, add it to selectedItems
        selectedItems.add(newItem);
    }


}