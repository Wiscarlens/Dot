package com.example.chezelisma;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 Created by Wiscarlens Lucius on 13 August 2023.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.DesignViewHolder> {
    private final ArrayList<String> orderNumber;
    private final ArrayList<String> orderDate;
    private final ArrayList<String> orderTime;
    private final ArrayList<String> orderStatus;
    private final ArrayList<String> orderTotalItems;
    private final ArrayList<String> orderTotalAmount;
    private final Context context;

    public OrdersAdapter(ArrayList<String> orderNumber, ArrayList<String> orderDate,
                         ArrayList<String> orderTime, ArrayList<String> orderStatus,
                         ArrayList<String> orderTotalItems, ArrayList<String> orderTotalAmount,
                         Context context) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
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
        holder.order_number.setText(orderNumber.get(position));
        holder.order_date.setText(orderDate.get(position));
        holder.order_time.setText(orderTime.get(position));
        holder.order_status.setText(orderStatus.get(position));
        holder.order_total_list.setText(orderTotalItems.get(position));
        holder.order_total_amount.setText(orderTotalAmount.get(position));
    }

    @Override
    public int getItemCount() {
        return orderNumber.size();
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        private final TextView order_number;
        private final TextView order_date;
        private final TextView order_time;
        private final TextView order_status;
        private final TextView order_total_list;
        private final TextView order_total_amount;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            order_number = itemView.findViewById(R.id.orderNumber);
            order_date = itemView.findViewById(R.id.orderDate);
            order_time = itemView.findViewById(R.id.orderTime);
            order_status = itemView.findViewById(R.id.orderStatus);
            order_total_list = itemView.findViewById(R.id.orderItems);
            order_total_amount = itemView.findViewById(R.id.orderTotal);
        }
    }
}
