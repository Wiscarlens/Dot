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
    ArrayList<Orders> ordersArrayList = new ArrayList<>();
    ArrayList<SelectedItems> selectedItemsArrayList = new ArrayList<>();
    OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect to Recyclerview in fragment_orders
        RecyclerView OrderList_RecyclerView = view.findViewById(R.id.ordersList);

        selectedItemsArrayList.add(new SelectedItems(R.drawable.coke));
        selectedItemsArrayList.add(new SelectedItems(R.drawable.fiji));
        selectedItemsArrayList.add(new SelectedItems(R.drawable.redbull));
        selectedItemsArrayList.add(new SelectedItems(R.drawable.gatorade));

        ordersArrayList.add(new Orders("55555", "8-13-2023",
                "5:14:20 PM", "Refund", 6,
                27.23, selectedItemsArrayList));

        ordersArrayList.add(new Orders("12346", "7-28-2023",
                "11:28:10 AM", "Competed", 25,
                147.33, selectedItemsArrayList));

        ordersAdapter = new OrdersAdapter(ordersArrayList, getContext());
        OrderList_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderList_RecyclerView.setAdapter(ordersAdapter);

    }
}