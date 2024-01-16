package com.module.dot.Activities.Home;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import static com.module.dot.Helpers.LocalFormat.getCurrentDateTime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.module.dot.Activities.ConfirmationFragment;
import com.module.dot.Activities.Items.Item;
import com.module.dot.Activities.Items.ItemGridAdapter;
import com.module.dot.Activities.MainActivity;
import com.module.dot.Activities.Orders.Order;
import com.module.dot.Activities.Transactions.Transaction;
import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.Database.Local.ItemDatabase;
import com.module.dot.Helpers.LocalFormat;
import com.module.dot.Helpers.ScannerManager;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private Button chargeButton;

    private final ArrayList<Item> itemList = new ArrayList<>();

    // TODO: Make selectedItem a set instead of an arraylist
    private final ArrayList<Item> selectedItems =  new ArrayList<>();

    // Select item total
    private final AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
    private String currentCharge;
    private Long totalItem = 0L;

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

        LinearLayout noData = view.findViewById(R.id.noDataHomeFragmentLL); // When Database is empty
        GridView itemGridview = view.findViewById(R.id.itemList);
        FloatingActionButton scanButton = view.findViewById(R.id.scanButton);
        chargeButton = view.findViewById(R.id.Charge);

        ScannerManager scannerManager = new ScannerManager(this);

        try (ItemDatabase itemDatabase = new ItemDatabase(getContext())) {
            if (itemDatabase.isTableEmpty("items")) {
                itemDatabase.showEmptyStateMessage(itemGridview, noData);
            } else {
                itemDatabase.showStateMessage(itemGridview, noData);
                itemDatabase.readItem(itemList); // Read data from database and save it the arraylist
            }
        } catch (Exception e) {
            Log.i("UserFragment", Objects.requireNonNull(e.getMessage()));
        }


        ItemGridAdapter itemGridAdapter = new ItemGridAdapter(itemList, getContext());
        itemGridview.setAdapter(itemGridAdapter);

        // When user select an item
        itemGridview.setOnItemClickListener((parent, view1, position, id) -> {
            // Find the selected item
            Item selectedItem = new Item(
                    itemList.get(position).getGlobalID(),
                    itemList.get(position).getName(),
                    itemList.get(position).getPrice(),
                    itemList.get(position).getSku(),
                    1L
            );

            // TODO: Optimize - All the line below can be part of addToSElected Item method
            double itemSelectedPrice = itemList.get(position).getPrice();

            totalItem ++;

            addToSelectedItems(selectedItem);
            updateAmount(itemSelectedPrice);

        });

        // When user click on charge button
        chargeButton.setOnClickListener(v -> {
            // Open bottom sheet layout
            Dialog dialog = showButtonDialog();

            final double bottomSheetHeight = 0.56; // Initialize to 56% of the screen height

            setBottomSheetHeight(dialog, bottomSheetHeight);
        });

        // When user click on scanner button
        scanButton.setOnClickListener(v -> {
                if (itemList.isEmpty()) {
                    Toast.makeText(fragmentActivity, "No item in database", Toast.LENGTH_SHORT).show();
                } else {
                    scannerManager.startBarcodeScanning(); // Scan barcode
                    String barcode = scannerManager.getScanItem(); // get barcode

                    for (Item item : itemList) {
                        if (barcode.equals(item.getSku())) {
                            addToSelectedItems(item); // Add the item to the selectedItems list
                            updateAmount(item.getPrice()); // Update Selected Item amount

                            break;
                        } else {
                            Toast.makeText(fragmentActivity, "Item not found", Toast.LENGTH_SHORT).show();
                        }
                    }





                    // Find the barcode in the database
//                    scanCode();  // Scan barcode to add item to cart
                }
            }
        );
    }

    public Dialog showButtonDialog(){
        final Dialog bottomSheetDialog = new Dialog(requireContext());

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

                            String[] dateTime = getCurrentDateTime(); // Get the current date and time

                           String orderGlobalID = FirebaseHandler.createOrder(new Order(
                                   MainActivity.currentUser.getCreatorID(),   // Creator ID
                                   dateTime[0],
                                   dateTime[1],
                                   totalPrice.get(), // Total amount
                                   totalItem, // Total item
                                   "Completed", // TODO: replace with the actual Order status
                                   selectedItems
                           ));

                            FirebaseHandler.readOrder("orders", getContext());

                            dateTime = getCurrentDateTime(); // Get the current date and time

                            // TODO: Create a new transaction

                            // Save data to firebase
                            FirebaseHandler.createTransaction( new Transaction(
                                    orderGlobalID, // Order ID
                                    "APPROVE", // TODO: replace with the actual transaction status
                                    totalPrice.get(),
                                    "visa", // TODO: replace with the actual payment method
                                    MainActivity.currentUser.getCreatorID(),
                                    dateTime[0],
                                    dateTime[1]
                            ));

                            FirebaseHandler.readTransaction("transactions", getContext());

                            // Sending order number to receipt fragment
                            Bundle orderNumberBundle = new Bundle();
                            orderNumberBundle.putString("orderNumber", orderGlobalID);
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
     * Result launcher for initiating barcode scanning and handling the scanning result.
     */
    ActivityResultLauncher<ScanOptions>  scannerLauncher = registerForActivityResult(new ScanContract(), result -> {
        String scanItem = result.getContents();

        for (Item item : itemList) {
             if (scanItem.equals(item.getSku())) {
                 addToSelectedItems(item); // Add the item to the selectedItems list
                 updateAmount(item.getPrice()); // Update Selected Item amount

                 break;
             } else {
                 Toast.makeText(fragmentActivity, "Item not found", Toast.LENGTH_SHORT).show();
             }
        }
    });

//    private boolean addScannedItem(String scanItem){
//        for (Item item : item_for_display) {
//            if (scanItem.equals(item.getSku())) {
////                return true;
//                addToSelectedItems(item); // Add the item to the selectedItems list
//                updateAmount(item.getPrice()); // Update Selected Item amount
//
////                break;
//            } else {
//                Toast.makeText(fragmentActivity, "Item not found", Toast.LENGTH_SHORT).show();
////                return false;
//            }
//        }
//
//        return false;
//    }

    /**
     * Adds an item to the selectedItems list or increases its frequency if it already exists.
     *
     * @param newItem The item to be added or whose frequency should be increased.
     */
    private void addToSelectedItems(Item newItem) {
        // Check if the item is already in selectedItems
        for (Item item : selectedItems) {
            if (Objects.equals(item.getGlobalID(), newItem.getGlobalID())) {
                // Item already exists, increase frequency
                item.setQuantity(item.getQuantity() + 1);

                return; // Exit the method since the item was found
            }
        }

        // Item not found, add it to selectedItems
        selectedItems.add(newItem);
    }

    private void updateAmount(double amount) {
        // Add selected item price together
        totalPrice.set(totalPrice.get() + amount); // Add the item price to the total price

        // Format the double value into currency format
        currentCharge = LocalFormat.getCurrencyFormat(totalPrice.get());

        // Set the button text to the current value of price
        chargeButton.setText(currentCharge);
    }


}