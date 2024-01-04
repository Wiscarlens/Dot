package com.module.dot.Activities.Transactions;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.Database.Local.TransactionDatabase;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;

public class TransactionsFragment extends Fragment {

    private final ArrayList<Transaction> transaction_for_display = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout noTransaction = view.findViewById(R.id.noTransactionFragmentLL); // When users Database is empty
        RecyclerView recyclerView = view.findViewById(R.id.transactionList);



        try(TransactionDatabase transactionDatabase = new TransactionDatabase(getContext())){
            try {
                if(!transactionDatabase.isTableExists("transactions")){
                    transactionDatabase.onCreate(transactionDatabase.getWritableDatabase()); // Create the database
                }

                FirebaseHandler.readTransaction("transactions", getContext());

            } catch (Exception e) {
                Log.i("TransactionFragment", Objects.requireNonNull(e.getMessage()));
            }

            if (transactionDatabase.isTableEmpty("transactions")) {
                transactionDatabase.showEmptyStateMessage(recyclerView, noTransaction);
            } else {
                transactionDatabase.showStateMessage(recyclerView, noTransaction);

                try {
                    transactionDatabase.readTransaction(transaction_for_display); // Read data from database and save it the arraylist

                    TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(transaction_for_display);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.i("TransactionFragment", Objects.requireNonNull(e.getMessage()));
                }
            }
        }


    }
}