package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private Button chargeButton;
    private ImageView noDataImage;
    private TextView noDataText;
    private GridView itemGridview;
    private ArrayList<Drawable> image = new ArrayList<>();

    private Map<Integer, String> itemId_and_name = new HashMap<>();

    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemPrice = new ArrayList<>();
    private ArrayList<String> itemUnitType = new ArrayList<>();
    private ArrayList<Integer> backgroundColor = new ArrayList<>();

    private ArrayList<String> selectProductName =  new ArrayList<>();
    private ArrayList<Double> selectProductPrice = new ArrayList<>();

    // Select item total
    private AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
    private String currentCharge;

    private MyDatabaseHelper myDB;

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

        noDataImage = view.findViewById(R.id.no_data_imageview); // When Database is empty
        noDataText = view.findViewById(R.id.no_data_textview); // When Database is empty
        itemGridview = view.findViewById(R.id.itemList);
        FloatingActionButton scanButton = view.findViewById(R.id.scanButton);
        chargeButton = view.findViewById(R.id.Charge);

        // Local database
        myDB = new MyDatabaseHelper(getContext());

        storeItemsDataInArrays(); // Save item data from database to the arraylist

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
                // Sending data to next fragment
                Bundle result = new Bundle();
                result.putString("price", currentCharge);
                getParentFragmentManager().setFragmentResult("priceData", result);

                int creatorId = 1;
                double totalAmount = totalPrice.get();
                String paymentMethod = "Visa";
                String paymentStatus = "Completed";
                int newOrderID = (int) myDB.addOrder(creatorId, totalAmount, paymentMethod, paymentStatus);

                // Find selectProductName ID in the database
                Integer[] itemID = getItemIdsFromNames(selectProductName);
                Integer[] itemFrequency = getFrequencies(selectProductName);

                for(int i = 0; i < itemID.length; i++){
                    myDB.addOrderItem(newOrderID, itemID[i], itemFrequency[i]);
                }

                // TODO: Find how to read data from the database using terminal

                // Open Confirmation fragment
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                fragmentTransaction.replace(R.id.fragment_container, confirmationFragment);
                fragmentTransaction.commit();

                //Toast.makeText(fragmentActivity, "Add to database", Toast.LENGTH_LONG).show();


//                CompletableFuture<Boolean> userResponse = showDialogMessage(); // Open confirmation fragment

//                userResponse.thenAcceptAsync(confirmed -> {
//                    if (confirmed) {
//                        // Adding order and transaction details to the database
//
//
//                        int creatorId = 1;
//                        double totalAmount = totalPrice.get();
//                        String paymentMethod = "Visa";
//                        String paymentStatus = "Completed";
//                        int newOrderID = (int) myDB.addOrder(creatorId, totalAmount, paymentMethod, paymentStatus);
//
//                        // Find selectProductName ID in the database
//                        Integer[] itemID = getItemIdsFromNames(selectProductName);
//                        Integer[] itemFrequency = getFrequencies(selectProductName);
//
//                        for(int i = 0; i < itemID.length; i++){
//                            myDB.addOrderItem(newOrderID, itemID[i], itemFrequency[i]);
//                        }
//
//
//                        // Open Confirmation fragment
//                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        ConfirmationFragment confirmationFragment = new ConfirmationFragment();
//                        fragmentTransaction.replace(R.id.fragment_container, confirmationFragment);
//                        fragmentTransaction.commit();
//
//                        Toast.makeText(fragmentActivity, "Add to database", Toast.LENGTH_LONG).show();
//                    } else {
//                        // User chose not to continue
//                        // Simulate showing a toast message
//                        Toast.makeText(getContext(), "Order cancelled.", Toast.LENGTH_SHORT).show();
//                    }
//                });

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


    private CompletableFuture<Boolean> showDialogMessage() {
        CompletableFuture<Boolean> checkoutFuture = new CompletableFuture<>();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getResources().getString(R.string.confirm))
                .setMessage(getResources().getString(R.string.confirm_checkout))
                .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                    checkoutFuture.complete(false);  // Complete with the value 'false'
                })
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    checkoutFuture.complete(true);   // Complete with the value 'true'

                }).show();

        return checkoutFuture;  // Return the CompletableFuture<Boolean>
    }


    private void storeItemsDataInArrays(){
        Cursor cursor = myDB.readAllItemsData();

        // In case database if empty
        if (cursor.getCount() == 0){
            itemGridview.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);

        } else{
            // Retrieve item data from the database
            while (cursor.moveToNext()){
                noDataImage.setVisibility(View.GONE);
                noDataText.setVisibility(View.GONE);
                itemGridview.setVisibility(View.VISIBLE);

                // get item image
                byte[] imageData = cursor.getBlob(1);

                // Convert the image byte array to a Bitmap
                Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Convert the Bitmap to a Drawable if needed
                Drawable itemImageDrawable = new BitmapDrawable(getResources(), itemImageBitmap);

                image.add(itemImageDrawable);

                // Add the ID and item name to the map
                itemId_and_name.put(cursor.getInt(0), cursor.getString(2));

                itemName.add(cursor.getString(2));
                itemPrice.add(cursor.getString(3));
                itemUnitType.add(cursor.getString(6));
                backgroundColor.add(R.color.white); // Default Background Color
            }
        }
    }

    /**
     * Retrieves the item IDs associated with given unique item names from the itemId_and_name map.
     *
     * @param itemNamesToFind The list of unique item names for which the corresponding item IDs are to be retrieved.
     * @return An array of item IDs corresponding to the provided unique item names. If an item name is not found, the corresponding array element will be null.
     */
    private Integer[] getItemIdsFromNames(ArrayList<String> itemNamesToFind) {
        // Remove duplicates from the itemNamesToFind ArrayList
        ArrayList<String> uniqueItemNamesToFind = new ArrayList<>(
                new LinkedHashSet<>(itemNamesToFind)); // Do not copy a value twice

        Integer[] itemIds = new Integer[uniqueItemNamesToFind.size()];

        for (int i = 0; i < uniqueItemNamesToFind.size(); i++) {
            String itemNameToFind = uniqueItemNamesToFind.get(i);

            for (Map.Entry<Integer, String> entry : itemId_and_name.entrySet()) {
                if (entry.getValue().equals(itemNameToFind)) {
                    itemIds[i] = entry.getKey();
                    break; // Move to the next item name
                }
            }

            // Log if an item name is not found in the map
            if (itemIds[i] == null) {
                Log.d("error", "HomeFragment: Item name '" + itemNameToFind + "' is not found in the map");
            }
        }

        return itemIds;
    }

    /**
     * Calculates the frequency of each name in the input ArrayList and returns an array of frequencies.
     *
     * @param selectProductName The ArrayList of strings containing the names to calculate frequencies for.
     * @return An array of integers representing the frequency of each name in the input ArrayList.
     */
    public static Integer[] getFrequencies(ArrayList<String> selectProductName) {
        // Create a HashMap to store the frequency of each name
        Map<String, Integer> nameFrequencyMap = new HashMap<>();

        // Count the frequency of each name
        for (String name : selectProductName) {
            nameFrequencyMap.put(name, nameFrequencyMap.getOrDefault(name, 0) + 1);
        }

        // Create an array to store the frequencies
        Integer[] frequencies = new Integer[selectProductName.size()];

        // Populate the array with frequencies
        for (int i = 0; i < selectProductName.size(); i++) {
            frequencies[i] = nameFrequencyMap.get(selectProductName.get(i));
        }

        return frequencies;
    }


}