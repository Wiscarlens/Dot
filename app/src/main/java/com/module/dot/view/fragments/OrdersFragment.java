package com.module.dot.view.fragments;

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

import com.module.dot.model.Order;
import com.module.dot.data.remote.FirebaseHandler;
import com.module.dot.data.local.OrderDatabase;
import com.module.dot.R;
import com.module.dot.view.adapters.OrdersAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class OrdersFragment extends Fragment {
    private final ArrayList<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseHandler.readOrder("orders", getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout noOrder = view.findViewById(R.id.noOrderFragmentLL); // When Database is empty
        RecyclerView orderRecyclerView = view.findViewById(R.id.ordersList);  // Connect to Recyclerview in fragment_orders

        try (OrderDatabase orderDatabase = new OrderDatabase(getContext())) {
            if(!orderDatabase.isTableExists("orders")){
                orderDatabase.onCreate(orderDatabase.getWritableDatabase()); // Create the database
            } else {
                if (orderDatabase.isTableEmpty("orders")) {
                    orderDatabase.showEmptyStateMessage(orderRecyclerView, noOrder);
                } else {
                    orderDatabase.showStateMessage(orderRecyclerView, noOrder);

                    orderDatabase.readOrder(orderList);

                    OrdersAdapter ordersAdapter = new OrdersAdapter(orderList, getContext());
                    orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    orderRecyclerView.setAdapter(ordersAdapter);
                }
            }

        } catch (Exception e) {
            Log.i("UserFragment", Objects.requireNonNull(e.getMessage()));
        }


    }
}