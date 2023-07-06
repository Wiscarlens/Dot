package com.example.chezelisma;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationFragment extends Fragment {
    private FragmentActivity fragmentActivity;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView status = view.findViewById(R.id.confirmationStatusTextView);
        TextView price = view.findViewById(R.id.confirmationPriceTextView);
        Button receipt = view.findViewById(R.id.printReceiptButton);
        Button newSales = view.findViewById(R.id.startNewSalesButton);

        // Receiving confirmation price from HomeFragment
        getParentFragmentManager().setFragmentResultListener("priceData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String data = result.getString("price");
                price.setText(data);
            }
        });


        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // When User click on receipt button
        receipt.setOnClickListener(v -> {
            ReceiptFragment receiptFragment = new ReceiptFragment();
            fragmentTransaction.replace(R.id.fragment_container, receiptFragment);
            fragmentTransaction.commit();
        });

        // When User click on Add New Sales Button
        newSales.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.commit();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }
}