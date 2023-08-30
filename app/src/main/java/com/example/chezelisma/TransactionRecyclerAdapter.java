package com.example.chezelisma;

import static com.example.chezelisma.LocalFormat.getCurrencyFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.DesignViewHolder> {
    private final ArrayList<Transactions> transactions;

    public TransactionRecyclerAdapter(ArrayList<Transactions> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        holder.dateTextView.setText(transactions.get(position).getTransactionDate());
        holder.timeTextView.setText(transactions.get(position).getTransactionTime());
        holder.orderNumberTextView.setText(transactions.get(position).getOrderNumber());
        holder.ID_TextView.setText(transactions.get(position).getTransactionID());
        holder.statusTextView.setText(transactions.get(position).getTransactionStatus());
        holder.totalTextView.setText(getCurrencyFormat(transactions.get(position).getTransactionTotal()));
        holder.paymentTypeImageView.setImageResource(transactions.get(position).getPaymentMethod());
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView timeTextView;
        private final TextView orderNumberTextView;
        private final TextView ID_TextView;
        private final TextView statusTextView;
        private final TextView totalTextView;
        private final ImageView paymentTypeImageView;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.transactionDate);
            timeTextView = itemView.findViewById(R.id.transactionTime);
            orderNumberTextView = itemView.findViewById(R.id.transactionOrderNumber);
            ID_TextView = itemView.findViewById(R.id.transactionID);
            statusTextView = itemView.findViewById(R.id.transactionStatus);
            totalTextView = itemView.findViewById(R.id.transactionTotal);
            paymentTypeImageView = itemView.findViewById(R.id.PaymentType);
        }
    }
}
