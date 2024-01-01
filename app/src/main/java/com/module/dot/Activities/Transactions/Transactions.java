package com.module.dot.Activities.Transactions;


public class Transactions {
    private String globalID;
    private String transactionID;
    private String creatorID;
    private String transactionDate;
    private String transactionTime;
    private final long orderNumber;
    private final String transactionStatus;
    private final double transactionTotal;
    private final String paymentMethod;

    // Constructor for displaying transactions
    public Transactions(String transactionDate, String transactionTime, long orderNumber,
                        String transactionID, String transactionStatus,
                        double transactionTotal, String paymentMethod) {
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
                        double transactionTotal, String paymentMethod, String creatorId, String date, String time) {
        this.orderNumber = orderNumber;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentMethod = paymentMethod;
        this.creatorID = creatorId;
        this.transactionDate = date;
        this.transactionTime = time;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public double getTransactionTotal() {
        return transactionTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
