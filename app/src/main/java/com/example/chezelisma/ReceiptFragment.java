package com.example.chezelisma;

import static com.example.chezelisma.LocalFormat.getCurrencyFormat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;


public class ReceiptFragment extends Fragment {
    private MutableLiveData<Long> orderNumberLiveData = new MutableLiveData<>();
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

        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());

        ArrayList<Orders> orders = new ArrayList<>();

        long order_number = 0L;


        // Receiving Order  Number from HomeFragment
        getParentFragmentManager().setFragmentResultListener(
                "orderNumberData",
                this,
                (requestKey, result) -> {

                    if (requestKey.equals("orderNumberData")) {
                        // The result is available
                        long orderNumberLong = result.getLong("orderNumber");

                        // Update the LiveData with the new value
                        orderNumberLiveData.setValue(orderNumberLong);
                    }

                    // TODO: Remove this log
                    //Log.d("Order Number", String.valueOf(result.getLong("orderNumber")));
                }
        );

        // Set up an observer to listen for changes to orderNumberLiveData
        orderNumberLiveData.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long order_number) {
                // This code will be executed whenever orderNumberLong changes
                MyDatabaseHelper.getOrdersDetails(
                        myDB,
                        orders,
                        getResources(),
                        order_number // Order Number
                );

                // Update UI elements with order data here
                if (!orders.isEmpty()) {
                    date.setText(orders.get(0).getOrderDate());
                    time.setText(orders.get(0).getOrderTime());
                    // TODO: Make total a string and format it to local currency in order class
                    total.setText(getCurrencyFormat(orders.get(0).getOrderTotalAmount()));
                    orderNumber.setText(String.valueOf(orders.get(0).getOrderNumber()));

                    ArrayList<Items> selectedItems = orders.get(0).getSelectedItem();

                    // Create the adapter and set it to the RecyclerView
                    BottomSheetAdapter receiptAdapter = new BottomSheetAdapter(selectedItems, getContext());
                    receiptItems.setAdapter(receiptAdapter);
                }


            }
        });


        // When user clicks on email button, send email to user with receipt
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Email Button Works", Toast.LENGTH_SHORT).show();
            }
        });

        // When user clicks on print button, print receipt
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Print Button Works", Toast.LENGTH_SHORT).show();
            }
        });

        // When user clicks on share button, share receipt
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Share Button Works", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

}