package com.module.dot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private final ArrayList<Orders> orders_for_display = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView noDataImage = view.findViewById(R.id.no_orders_imageview); // When Database is empty
        TextView noDataText = view.findViewById(R.id.no_orders_textview); // When Database is empty
        RecyclerView OrderList_RecyclerView = view.findViewById(R.id.ordersList);  // Connect to Recyclerview in fragment_orders

        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext()); // Local database

        MyDatabaseHelper.getOrders(
                myDB, // Local database
                orders_for_display, // ArrayList to store Orders objects for display
                OrderList_RecyclerView, // RecyclerView UI element to display orders
                noDataImage, // ImageView UI element to show when no data is available
                noDataText, // TextView UI element to show when no data is available
                getResources() // Resources instance to access app resources
        );

        OrdersAdapter ordersAdapter = new OrdersAdapter(orders_for_display, getContext());
        OrderList_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderList_RecyclerView.setAdapter(ordersAdapter);
    }
}