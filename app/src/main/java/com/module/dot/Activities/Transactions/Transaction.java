package com.module.dot.Activities.Transactions;


public class Transaction {
    private String globalID;
    private String localID;
    private String creatorID;
    private String transactionDate;
    private String transactionTime;
    private String orderGlobalID;
    private long orderNumber;
    private String transactionStatus;
    private double transactionTotal;
    private String paymentMethod;

    public Transaction() {
    }

    // Constructor for displaying transactions
    public Transaction( String globalID, long orderNumber, String transactionDate,
                        String transactionTime, String transactionStatus,
                       double transactionTotal, String paymentMethod) {
        this.globalID = globalID;
        this.orderNumber = orderNumber;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.transactionTotal = transactionTotal;
        this.paymentMethod = paymentMethod;
    }

    // Constructor for creating a new transaction
    public Transaction(String orderGlobalID, String transactionStatus,
                       double transactionTotal, String paymentMethod, String creatorId, String date, String time) {
        this.orderGlobalID = orderGlobalID;
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

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
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

    public String getOrderGlobalID() {
        return orderGlobalID;
    }

    public void setOrderGlobalID(String orderGlobalID) {
        this.orderGlobalID = orderGlobalID;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public double getTransactionTotal() {
        return transactionTotal;
    }

    public void setTransactionTotal(double transactionTotal) {
        this.transactionTotal = transactionTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
