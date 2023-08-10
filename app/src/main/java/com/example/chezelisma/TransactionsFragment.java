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

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    private ArrayList<String> transactionDate = new ArrayList<>();
    private ArrayList<String> transactionTime = new ArrayList<>();
    private ArrayList<String> orderNumber = new ArrayList<>();
    private ArrayList<String> transactionID = new ArrayList<>();
    private ArrayList<String> transactionStatus = new ArrayList<>();
    private ArrayList<String> transactionTotal = new ArrayList<>();
    private ArrayList<Integer> paymentType = new ArrayList<>();

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

        // Connect to Recyclerview in fragment_users
        RecyclerView recyclerView = view.findViewById(R.id.transactionList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add transaction
        transactionDate.add("04/27/2023");
        transactionTime.add("9:52:12 PM");
        orderNumber.add("# 67387");
        transactionID.add("ZAC19945678912334567");
        transactionStatus.add("APPROVE");
        transactionTotal.add("$ 15.23");
        paymentType.add(R.drawable.baseline_money_24);

        transactionDate.add("03/07/2022");
        transactionTime.add("10:32:14 AM");
        orderNumber.add("# 97487");
        transactionID.add("XXC19945678912334567");
        transactionStatus.add("PENDING");
        transactionTotal.add("$ 5.53");
        paymentType.add(R.drawable.amex);

        transactionDate.add("03/07/2022");
        transactionTime.add("10:32:14 AM");
        orderNumber.add("# 97487");
        transactionID.add("AAC19945678912334567");
        transactionStatus.add("REFUNDED");
        transactionTotal.add("$ 3.45");
        paymentType.add(R.drawable.visa);

        transactionDate.add("03/07/2022");
        transactionTime.add("10:32:14 AM");
        orderNumber.add("# 97487");
        transactionID.add("MMC19945678912334567");
        transactionStatus.add("FAILED");
        transactionTotal.add("$ 35.89");
        paymentType.add(R.drawable.mastercard);

        transactionDate.add("03/07/2022");
        transactionTime.add("10:32:14 AM");
        orderNumber.add("# 97487");
        transactionID.add("DDC19945678912334567");
        transactionStatus.add("DECLINED");
        transactionTotal.add("$ 69.99");
        paymentType.add(R.drawable.discover);

        TransactionRecyclerAdapter adapter = new TransactionRecyclerAdapter(transactionDate, transactionTime, orderNumber,
                transactionID, transactionStatus, transactionTotal, paymentType, getContext());

        recyclerView.setAdapter(adapter);


    }
}