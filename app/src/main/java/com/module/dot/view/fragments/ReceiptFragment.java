package com.module.dot.view.fragments;

import static com.module.dot.utils.LocalFormat.getCurrencyFormat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.view.MainActivity;
import com.module.dot.view.adapters.OrderItemAdapter;
import com.module.dot.model.Item;
import com.module.dot.model.Order;
import com.module.dot.data.local.OrderDatabase;
import com.module.dot.view.utils.BarcodeManager;
import com.module.dot.utils.PDFManager;
import com.module.dot.utils.Utils;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class ReceiptFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AtomicReference<Order> order = new AtomicReference<>(new Order());


        LinearLayout receiptLayout = view.findViewById(R.id.receiptFooter);

        ImageView logo = view.findViewById(R.id.receiptLogo);
        TextView companyName = view.findViewById(R.id.receiptCompanyName);
        TextView companyAddress = view.findViewById(R.id.receiptCompanyStreetName);
        TextView companyCity = view.findViewById(R.id.receiptCompanyCityStZipcode);
        TextView date = view.findViewById(R.id.receiptDate);
        TextView time = view.findViewById(R.id.receiptTime);

        RecyclerView receiptItems = view.findViewById(R.id.receiptItems);

        TextView subtotal = view.findViewById(R.id.receiptSubtotal);
        TextView tax = view.findViewById(R.id.receiptTax);
        TextView total = view.findViewById(R.id.receiptTotal);

        ImageView barcode = view.findViewById(R.id.receiptBarcode);
        TextView orderNumber = view.findViewById(R.id.receiptOrderNumber);

        TextView emailButton = view.findViewById(R.id.receiptEmailButton);
        TextView printButton = view.findViewById(R.id.receiptPrintButton);
        TextView shareButton = view.findViewById(R.id.receiptShareButton);

        receiptItems.setLayoutManager(new LinearLayoutManager(getContext()));



        // Receiving Order  Number from HomeFragment
        getParentFragmentManager().setFragmentResultListener(
                "orderNumberData",
                this,
                (requestKey, result) -> {
                    try (OrderDatabase orderDatabase = new OrderDatabase(getContext())){
                        Log.d("Order Number", Objects.requireNonNull(result.getString("orderNumber")));

                        // TODO:  the bug happen here
                        order.set(orderDatabase.getOrderDetails(result.getString("orderNumber"))); // Order Number

                        // Update UI elements with order data here
                        if (order != null) {

//                        Log.d("Order Number", order.get().getCreatorID());

                            // Format the order number to have at least 5 digits
                            String orderNumberString = order.get().getGlobalID();

                            // Generate the barcode from order number
                            Drawable barcodeDrawable = BarcodeManager.generateBarcode(
                                    orderNumberString,
                                    getContext()
                            );

                            // logo.setImageDrawable();
                            companyName.setText(MainActivity.currentUser.getCompanyName());
                            companyAddress.setText("PO BOX 568153");
                            companyCity.setText("Orlando, FL 32856");
                            subtotal.setText(getCurrencyFormat(order.get().getOrderTotalAmount())); // TODO: Replace with subtotal
                            tax.setText("0.00");
                            date.setText(order.get().getOrderDate());
                            time.setText(order.get().getOrderTime());
                            // TODO: Make total a string and format it to local currency in order class
                            total.setText(getCurrencyFormat(order.get().getOrderTotalAmount()));
                            barcode.setImageDrawable(barcodeDrawable);
                            orderNumber.setText(orderNumberString);

                            ArrayList<Item> selectedItems = order.get().getSelectedItemList();

                            Log.d("Order Number", "onViewCreated: " + selectedItems.get(0).getName());

                            // Create the adapter and set it to the RecyclerView
                            OrderItemAdapter receiptAdapter = new OrderItemAdapter(selectedItems, getContext());
                            receiptItems.setAdapter(receiptAdapter);
                        } else {
                            // TODO: Show error message that receipt could not be generated
                            Log.e("Order Number", "Order is null");
                        }

                    } catch (Exception e) {
                        Log.i("ReceiptFragment", Objects.requireNonNull(e.getMessage()));
                    }




                }
        );


        // When user clicks on email button, send email to user with receipt
        emailButton.setOnClickListener(v -> Toast.makeText(getContext(), "Email Button Works", Toast.LENGTH_SHORT).show());

        // When user clicks on print button, print receipt
        printButton.setOnClickListener(v -> {
                    String orderNumberString = Utils.formatOrderNumber(order.get().getOrderNumber());

                    PDFManager.createPDF(orderNumberString, view, receiptLayout, getContext());

                    Toast.makeText(getContext(), "Print", Toast.LENGTH_SHORT).show();

            }

        );

        // When user clicks on share button, share receipt
        shareButton.setOnClickListener(v -> Toast.makeText(getContext(), "Share Button Works", Toast.LENGTH_SHORT).show());

        }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

}