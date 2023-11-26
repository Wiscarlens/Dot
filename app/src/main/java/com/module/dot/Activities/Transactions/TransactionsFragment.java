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

import com.module.dot.Database.Local.TransactionDatabase;
import com.module.dot.Database.Local.UserDatabase;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Objects;

public class TransactionsFragment extends Fragment {

    private final ArrayList<Transactions> transactions_for_display = new ArrayList<>();

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

//        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext()); // Local database

        try (TransactionDatabase transactionDatabase = new TransactionDatabase(getContext())) {
            if(!transactionDatabase.isTableExists("transactions")){
                transactionDatabase.onCreate(transactionDatabase.getWritableDatabase()); // Create the database
                transactionDatabase.showEmptyStateMessage(recyclerView, noTransaction);
                return;
            } else {
                if (transactionDatabase.isTableEmpty("transactions")) {
                    transactionDatabase.showEmptyStateMessage(recyclerView, noTransaction);
                } else {
                    transactionDatabase.showStateMessage(recyclerView, noTransaction);

                    transactionDatabase.readTransaction(transactions_for_display); // Read data from database and save it the arraylist
                }
            }

        } catch (Exception e) {
            Log.i("TransactionFragment", Objects.requireNonNull(e.getMessage()));
        }


        TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(transactions_for_display);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);


    }
}