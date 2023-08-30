package com.example.chezelisma;


import java.util.ArrayList;

public class Orders {
    private long orderNumber;
    private String orderDate;
    private String orderTime;
    private final String orderStatus;
    private int orderTotalItems;
    private final double orderTotalAmount;
    private String creatorID;

    private ArrayList<Items> selectedItem;

    // Constructor for displaying orders
    public Orders(long orderNumber, String orderDate, String orderTime, String orderStatus,
                  int orderTotalItems, double orderTotalAmount, ArrayList<Items> selectedItem) {
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

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public int getOrderTotalItems() {
        return orderTotalItems;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public ArrayList<Items> getSelectedItem() {
        return selectedItem;
    }
}
