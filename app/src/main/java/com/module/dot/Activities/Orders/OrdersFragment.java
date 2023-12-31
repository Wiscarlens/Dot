package com.module.dot.Activities.Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.Database.Local.OrderDatabase;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;

public class OrdersFragment extends Fragment {
    private final ArrayList<Orders> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout noOrder = view.findViewById(R.id.noOrderFragmentLL); // When Database is empty
        RecyclerView OrderList_RecyclerView = view.findViewById(R.id.ordersList);  // Connect to Recyclerview in fragment_orders

        try (OrderDatabase orderDatabase = new OrderDatabase(getContext())) {
            if(!orderDatabase.isTableExists("orders")){
                orderDatabase.onCreate(orderDatabase.getWritableDatabase()); // Create the database
                return;
            } else {
                if (orderDatabase.isTableEmpty("orders")) {
                    orderDatabase.showEmptyStateMessage(OrderList_RecyclerView, noOrder);
                } else {
                    orderDatabase.showStateMessage(OrderList_RecyclerView, noOrder);

                    orderDatabase.readOrder(orderList);
                }
            }

        } catch (Exception e) {
            Log.i("UserFragment", Objects.requireNonNull(e.getMessage()));
        }

        OrdersAdapter ordersAdapter = new OrdersAdapter(orderList, getContext());
        OrderList_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderList_RecyclerView.setAdapter(ordersAdapter);
    }
}