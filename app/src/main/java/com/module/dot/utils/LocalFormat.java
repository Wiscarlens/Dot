package com.module.dot.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalFormat {
    // System will check what language and country the device is set on
    // System will cache the data

    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);

    // Currency
    private static final Locale localCountry = Locale.US;
//    private static final Locale localCountry = Locale.FRANCE;
//    private static final Locale localCountry = new Locale("fr", "HT");


    public static String getCurrencyFormat(double amount){
        NumberFormat usDollarFormat = NumberFormat.getCurrencyInstance(localCountry);
        return usDollarFormat.format(amount);
    }


    /**
     *
     * @return An array containing the formatted date and time. The first element
     *         in the array is the date and the second element is the time.
     */
    public static String[] getCurrentDateTime() {
        // Get the current date and time.
        Date date = new Date();

        // Get the formatted date and time.
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        // Create an array to store the formatted date and time.
        String[] dateTime = new String[2];

        dateTime[0] = formattedDate;
        dateTime[1] = formattedTime;

        // Return the array.
        return dateTime;
    }

}
