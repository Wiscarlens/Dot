package com.module.dot.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.module.dot.Activities.Home.HomeFragment;
import com.module.dot.R;

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

        LottieAnimationView animationView = view.findViewById(R.id.confirmationAnimationView);
        TextView status = view.findViewById(R.id.confirmationStatusTextView);
        TextView price = view.findViewById(R.id.confirmationPriceTextView);
        Button receipt = view.findViewById(R.id.printReceiptButton);
        Button newSales = view.findViewById(R.id.startNewSalesButton);

        // Receiving confirmation price from HomeFragment
        getParentFragmentManager().setFragmentResultListener(
                "priceData",
                this,
                (requestKey, result) -> {
                    animationView.playAnimation();
                    String data = result.getString("price");
                    price.setText(data);
                    // TODO: get text from string.xml
                    status.setText("Payment Successful");
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