package com.example.chezelisma;

import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 Created by
 @author Wiscarlens Lucius
 @since 8 May 2023.
 */

public interface OnBottomSheetHomeUpdateListener {
    void updateBottomSheetView(ArrayList<String> productName, ArrayList<Integer> productTotal, ArrayList<String> productPrice);

    void onUpdateTotalTransaction(TextView totalCharge, String price);

    void onUpdateCheckoutButton(Button checkoutButton);

}
