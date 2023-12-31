package com.module.dot.Activities.Orders;


import com.module.dot.Activities.Items.Item;

import java.util.ArrayList;

public class Orders {
    private long orderNumber;
    private String globalID;
    private String orderDate;
    private String orderTime;
    private final String orderStatus;
    private int orderTotalItems;
    private final double orderTotalAmount;
    private String creatorID;

    private ArrayList<Item> selectedItem;

    // Constructor for displaying orders
    public Orders(long orderNumber, String orderDate, String orderTime, String orderStatus,
                  int orderTotalItems, double orderTotalAmount, ArrayList<Item> selectedItem) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItem = selectedItem;
    }

    // Constructor for creating a new order
    public Orders(String creatorID, double orderTotalAmount, String orderStatus) {
        this.creatorID = creatorID;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
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
        return orderTotalItems;
    }

    public void setOrderTotalItems(int orderTotalItems) {
        this.orderTotalItems = orderTotalItems;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public ArrayList<Item> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ArrayList<Item> selectedItem) {
        this.selectedItem = selectedItem;
    }
}
