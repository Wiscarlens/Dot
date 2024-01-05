package com.module.dot.Activities.Orders;


import com.module.dot.Activities.Items.Item;

import java.util.ArrayList;

public class Order {
    private String globalID;
    private long orderNumber;
    private String orderDate;
    private String orderTime;
    private String orderStatus;
    private int orderTotalItems;
    private double orderTotalAmount;
    private String creatorID;

    private ArrayList<Item> selectedItemList;

    public Order() {
    }

    // Constructor for displaying orders
    public Order(long orderNumber, String orderDate, String orderTime, String orderStatus,
                 int orderTotalItems, double orderTotalAmount, ArrayList<Item> selectedItemList) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItemList = selectedItemList;
    }

    // Constructor for creating a new order
    public Order(String globalID, String creatorID, String date, String time, double orderTotalAmount, String orderStatus) {
        this.globalID = globalID;
        this.creatorID = creatorID;
        this.orderDate = date;
        this.orderTime = time;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
    }

    // Constructor for creating a new order
    public Order(String creatorID, String date, String time, double orderTotalAmount,
                 String orderStatus, ArrayList<Item> selectedItemList) {
        this.creatorID = creatorID;
        this.orderDate = date;
        this.orderTime = time;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItemList = selectedItemList;
    }


    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public int getOrderTotalItems() {
        return selectedItemList.size();
    }

    public void setOrderTotalItems(int orderTotalItems) {
        this.orderTotalItems = orderTotalItems;
    }

    public double getOrderTotalAmount() {
        selectedItemList.get(0).getPrice();
        return orderTotalAmount;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public ArrayList<Item> getSelectedItemList() {
        return selectedItemList;
    }

    public void setSelectedItemList(ArrayList<Item> selectedItemList) {
        this.selectedItemList = selectedItemList;
    }
}
