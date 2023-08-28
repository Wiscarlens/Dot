package com.example.chezelisma;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    private ArrayList<Transactions> transactions = new ArrayList<>();

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentActivity fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView noUserImage = view.findViewById(R.id.no_transaction_imageview); // When users Database is empty
        TextView noUserText = view.findViewById(R.id.no_transaction_textview); // When users Database is empty
        RecyclerView recyclerView = view.findViewById(R.id.transactionList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Transactions transaction = new Transactions(
                "04/27/2023",
                "9:52:12 PM",
                "# 67387",
                "ZAC19945678912334567",
                "APPROVE",
                15.23,
                R.drawable.baseline_money_24
        );

        transactions.add(transaction);

        TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(transactions, getContext());

        recyclerView.setAdapter(adapter);


    }
}