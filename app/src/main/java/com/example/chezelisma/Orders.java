package com.example.chezelisma;


import java.util.ArrayList;

public class Orders {
    private final long orderNumber;
    private final String orderDate;
    private final String orderTime;
    private final String orderStatus;
    private final int orderTotalItems;
    private final double orderTotalAmount;

    private final ArrayList<Items> selectedItem;

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

    public ArrayList<Items> getSelectedItem() {
        return selectedItem;
    }
}
