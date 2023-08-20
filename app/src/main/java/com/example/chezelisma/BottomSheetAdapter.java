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
import java.util.Map;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomViewHolder>{
    private ArrayList<String> productName;
    private ArrayList<Double> productPrice;
    private Items[] product;
    Map<String, Integer> name_and_frequency;

    private Context context;

    public BottomSheetAdapter(ArrayList<String> name, ArrayList<Double> price, Context context) {
        this.productName = name;
        this.productPrice = price;
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
        name_and_frequency = SelectedProductUtils.getProductFrequency(productName);
        return name_and_frequency.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {
        // ******************Need to optimize
        name_and_frequency = SelectedProductUtils.getProductFrequency(productName);
        Map<String, Double> name_and_price = SelectedProductUtils.combinePriceName(productName, productPrice);

        product = SelectedProductUtils.getItemsAsArray(name_and_frequency, name_and_price);

        // Convert to String
        String tempFrequency =  String.valueOf(product[position].getFrequency());
        String tempPrice = CurrencyFormat.getCurrencyFormat(product[position].getPrice());

        holder.productNameTextView.setText(product[position].getName());
        holder.frequencyTextView.setText(tempFrequency);
        holder.priceTextView.setText(tempPrice);

        // When User click in a product in bottom sheet
        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "You selected " + product[position].getName(), Toast.LENGTH_SHORT).show());
    }


    public class BottomViewHolder extends RecyclerView.ViewHolder {
        private final TextView productNameTextView;
        private final TextView frequencyTextView;
        private final TextView priceTextView;
        private final CardView cardView;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productName_design);
            frequencyTextView = itemView.findViewById(R.id.unitTotal_design);
            priceTextView = itemView.findViewById(R.id.unitPrice_design);
            cardView = itemView.findViewById(R.id.bottomSheetDesignCardView);

        }
    }

}
