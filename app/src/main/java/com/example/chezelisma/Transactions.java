package com.example.chezelisma;

import android.graphics.drawable.Drawable;

public class Transactions {
    private final String transactionID;
    private final String transactionDate;
    private final String transactionTime;
    private final String orderNumber;
    private final String transactionStatus;
    private final double transactionTotal;
    private final int paymentType;

    public Transactions(String transactionDate, String transactionTime, String orderNumber, String transactionID, String transactionStatus, double transactionTotal, int paymentType) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.orderNumber = orderNumber;
        this.transactionID = transactionID;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentType = paymentType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public double getTransactionTotal() {
        return transactionTotal;
    }

    public int getPaymentType() {
        return paymentType;
    }
}
