package com.module.dot.Activities.Transactions;


public class Transactions {
    private String transactionID;
    private String transactionDate;
    private String transactionTime;
    private final long orderNumber;
    private final String transactionStatus;
    private final double transactionTotal;
    private final int paymentMethod;

    // Constructor for displaying transactions
    public Transactions(String transactionDate, String transactionTime, long orderNumber,
                        String transactionID, String transactionStatus,
                        double transactionTotal, int paymentMethod) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.orderNumber = orderNumber;
        this.transactionID = transactionID;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentMethod = paymentMethod;
    }

    // Constructor for creating a new transaction
    public Transactions(long orderNumber, String transactionStatus,
                        double transactionTotal, int paymentMethod) {
        this.orderNumber = orderNumber;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public long getOrderNumber() {
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

    public int getPaymentMethod() {
        return paymentMethod;
    }
}
