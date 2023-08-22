package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomViewHolder>{
    private final ArrayList<Items> items;
    private final Context context;

    public BottomSheetAdapter(ArrayList<Items> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_design_bottom_sheet, parent, false);
        return new BottomViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {
        holder.itemNameTextView.setText(items.get(position).getName());
        holder.priceTextView.setText(String.valueOf(items.get(position).getPrice()));
        holder.frequencyTextView.setText(String.valueOf(items.get(position).getFrequency()));

        // When User click in a product in bottom sheet
        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "You selected " + items.get(position).getName(), Toast.LENGTH_SHORT).show());
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameTextView;
        private final TextView frequencyTextView;
        private final TextView priceTextView;
        private final CardView cardView;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.productName_design);
            frequencyTextView = itemView.findViewById(R.id.unitTotal_design);
            priceTextView = itemView.findViewById(R.id.unitPrice_design);
            cardView = itemView.findViewById(R.id.bottomSheetDesignCardView);

        }
    }

}
