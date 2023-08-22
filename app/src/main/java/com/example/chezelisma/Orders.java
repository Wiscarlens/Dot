package com.example.chezelisma;

import java.util.ArrayList;

public class Orders {
    String orderNumber;
    String orderDate;
    String orderTime;
    String orderStatus;
    int orderTotalItems;
    double orderTotalAmount;

    ArrayList<Items> selectedItem;

    public Orders(String orderNumber, String orderDate, String orderTime,
                  String orderStatus, int orderTotalItems, double orderTotalAmount,
                  ArrayList<Items> selectedItem) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItem = selectedItem;
    }
}
