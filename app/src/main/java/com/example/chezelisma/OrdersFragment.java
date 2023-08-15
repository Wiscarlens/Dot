package com.example.chezelisma;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private ArrayList<String> orderNumber = new ArrayList<>();
    private ArrayList<String> orderDate = new ArrayList<>();
    private ArrayList<String> orderTime = new ArrayList<>();
    private ArrayList<String> orderStatus = new ArrayList<>();
    private ArrayList<String> orderTotalItems = new ArrayList<>();
    private ArrayList<String> orderTotalAmount = new ArrayList<>();
    private Object[][] ItemImage; // Selected item images


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect to Recyclerview in fragment_orders
        RecyclerView OrderListView = view.findViewById(R.id.ordersList);

        OrderListView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderNumber.add("# 55555");
        orderDate.add("8-13-2023");
        orderTime.add("5:14:20 PM");
        orderStatus.add("Refund");
        orderTotalItems.add("6 Items");
        orderTotalAmount.add("$ 27.23");

        orderNumber.add("# 12346");
        orderDate.add("7-28-2023");
        orderTime.add("11:28:10 AM");
        orderStatus.add("Competed");
        orderTotalItems.add("25 Items");
        orderTotalAmount.add("$ 147.33");

        OrdersAdapter ordersAdapter = new OrdersAdapter(orderNumber, orderDate, orderTime,
                orderStatus, orderTotalItems, orderTotalAmount, getContext());

        OrderListView.setAdapter(ordersAdapter);

    }
}