package com.example.chezelisma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.DesignViewHolder> {

    private ArrayList<String> transactionDate;
    private ArrayList<String> transactionTime;
    private ArrayList<String> orderNumber;
    private ArrayList<String> transactionID;
    private ArrayList<String> transactionStatus;
    private ArrayList<String> transactionTotal;
    private ArrayList<Integer> paymentType;

    private Context context;

    public TransactionRecyclerAdapter(ArrayList<String> transactionDate, ArrayList<String> transactionTime,
                                      ArrayList<String> orderNumber, ArrayList<String> transactionID,
                                      ArrayList<String> transactionStatus, ArrayList<String> transactionTotal,
                                      ArrayList<Integer> paymentType, Context context) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.orderNumber = orderNumber;
        this.transactionID = transactionID;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentType = paymentType;
        this.context = context;// **
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return transactionID.size();
    }

    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        holder.dateTextView.setText(transactionDate.get(position));
        holder.timeTextView.setText(transactionTime.get(position));
        holder.orderNumberTextView.setText(orderNumber.get(position));
        holder.ID_TextView.setText(transactionID.get(position));
        holder.statusTextView.setText(transactionStatus.get(position));
        holder.totalTextView.setText(transactionTotal.get(position));
        holder.paymentTypeImageView.setImageResource(paymentType.get(position));
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView orderNumberTextView;
        private TextView ID_TextView;
        private TextView statusTextView;
        private TextView totalTextView;
        private ImageView paymentTypeImageView;

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
