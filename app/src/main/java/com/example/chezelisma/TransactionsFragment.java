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

    private final ArrayList<Transactions> transactions_for_display = new ArrayList<>();

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


        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext()); // Local database

        MyDatabaseHelper.getTransactions(
                myDB,
                transactions_for_display,
                recyclerView,
                noUserImage,
                noUserText
        );


        TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(transactions_for_display);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);


    }
}