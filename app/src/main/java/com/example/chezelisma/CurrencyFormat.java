package com.example.chezelisma;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    // System will check what language and country the device is set on
    // System will cache the data

    private static final Locale localCountry = Locale.US;
    //private static final Locale localCountry = Locale.FRANCE;
    //private static final Locale localCountry = new Locale("fr", "HT");

    public static String getCurrencyFormat(double amount){
        NumberFormat usDollarFormat = NumberFormat.getCurrencyInstance(localCountry);
        return usDollarFormat.format(amount);
    }
}
