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
import java.util.HashMap;
import java.util.Map;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomViewHolder>{
    private ArrayList<String> productName;
    private ArrayList<Integer> productTotal;
    private ArrayList<String> productPrice;
    

    private Context context;

    public BottomSheetAdapter(ArrayList<String> productName, ArrayList<Integer> productTotal,
                              ArrayList<String> productPrice, Context context) {
        this.productName = productName;
        this.productTotal = productTotal;
        this.productPrice = productPrice;
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
        return productName.size();
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {
        holder.productTextView.setText(productName.get(position));
        holder.totalTextView.setText(String.valueOf(productTotal.get(position))); // Convert to String
        holder.priceTextView.setText(productPrice.get(position));

        holder.cardView.setOnClickListener(v -> {
            Toast.makeText(context, "You selected " + productName.get(position), Toast.LENGTH_SHORT).show();
        });
    }


    public class BottomViewHolder extends RecyclerView.ViewHolder {
        private TextView productTextView;
        private TextView totalTextView;
        private TextView priceTextView;
        private CardView cardView;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);

            productTextView = itemView.findViewById(R.id.productName_design);
            totalTextView = itemView.findViewById(R.id.unitTotal_design);
            priceTextView = itemView.findViewById(R.id.unitPrice_design);
            cardView = itemView.findViewById(R.id.bottomSheetDesignCardView);

        }
    }

    public static Map<String, Integer> getProductFrequency(ArrayList<String> itemNames) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Iterate through each item name
        for (String itemName : itemNames) {
            // If the item name is already in the map, increment its frequency
            if (frequencyMap.containsKey(itemName)) {
                int frequency = frequencyMap.get(itemName);
                frequencyMap.put(itemName, frequency + 1);
            }
            // Otherwise, add the item name to the map with a frequency of 1
            else {
                frequencyMap.put(itemName, 1);
            }
        }

        return frequencyMap;
    }
}
