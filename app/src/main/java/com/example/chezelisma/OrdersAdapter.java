package com.example.chezelisma;

import static com.example.chezelisma.LocalFormat.getCurrencyFormat;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 Created by Wiscarlens Lucius on 13 August 2023.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.DesignViewHolder> {
    private  final ArrayList<Orders> ordersArrayList;
    private final Context context;

    public OrdersAdapter(ArrayList<Orders> ordersArrayList, Context context) {
        this.ordersArrayList = ordersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersAdapter.DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.orders_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.DesignViewHolder holder, int position) {
        holder.order_number.setText(String.valueOf(ordersArrayList.get(position).getOrderNumber()));
        holder.order_date.setText(ordersArrayList.get(position).getOrderDate());
        holder.order_time.setText(ordersArrayList.get(position).getOrderTime());
        holder.order_status.setText(ordersArrayList.get(position).getOrderStatus());
        holder.order_total_items.setText(String.valueOf(ordersArrayList.get(position).getOrderTotalItems()));
        holder.order_total_amount.setText(getCurrencyFormat(ordersArrayList.get(position).getOrderTotalAmount()));

        SelectedItemsAdapter selectedItemsAdapter = new SelectedItemsAdapter(ordersArrayList.get(position).getSelectedItem(), context);

        holder.selectedItemRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.selectedItemRecyclerView.setAdapter(selectedItemsAdapter);

    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        private final TextView order_number;
        private final TextView order_date;
        private final TextView order_time;
        private final TextView order_status;
        private final TextView order_total_items;
        private final TextView order_total_amount;
        private final RecyclerView selectedItemRecyclerView;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            order_number = itemView.findViewById(R.id.orderNumber);
            order_date = itemView.findViewById(R.id.orderDate);
            order_time = itemView.findViewById(R.id.orderTime);
            order_status = itemView.findViewById(R.id.orderStatus);
            order_total_items = itemView.findViewById(R.id.orderTotalItems);
            order_total_amount = itemView.findViewById(R.id.orderTotal);
            selectedItemRecyclerView = itemView.findViewById(R.id.selectedItemRV);
        }
    }
}
