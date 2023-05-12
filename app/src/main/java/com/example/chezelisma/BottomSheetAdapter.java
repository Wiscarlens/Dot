package com.example.chezelisma;

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

public class BottomSheetAdapter {
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


    public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_design_bottom_sheet, parent, false);
        return new BottomViewHolder(view);
    }


    public int getItemCount() {
        return productName.size();
    }

    public void onBindViewHolder(@NonNull BottomSheetAdapter.BottomViewHolder holder, int position) {
        holder.productTextView.setText(productName.get(position));
        holder.totalTextView.setText(productTotal.get(position));
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
}
