package com.module.dot.Activities.Home;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import static com.module.dot.Helpers.LocalFormat.getCurrencyFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.Activities.Items.Item;
import com.module.dot.R;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.BottomViewHolder>{
    private final ArrayList<Item> items;
    private final Context context;

    public OrderItemAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_design, parent, false);
        return new BottomViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {
        holder.itemNameTextView.setText(items.get(position).getName());
        holder.unitPriceTextView.setText(String.valueOf(items.get(position).getPrice()));
        holder.frequencyTextView.setText(String.valueOf(items.get(position).getQuantity()));
        holder.totalPriceTextView.setText(getCurrencyFormat(items.get(position).getPrice() * items.get(position).getQuantity()));

        // When User click in a product in bottom sheet
        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "You selected " + items.get(position).getName(), Toast.LENGTH_SHORT).show());
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameTextView;
        private final TextView frequencyTextView;
        private final TextView unitPriceTextView;
        private final TextView totalPriceTextView;
        private final CardView cardView;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.productName_design);
            frequencyTextView = itemView.findViewById(R.id.unitTotal_design);
            unitPriceTextView = itemView.findViewById(R.id.unitPrice_design);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice_design);
            cardView = itemView.findViewById(R.id.bottomSheetDesignCardView);

        }
    }

}
