package com.example.chezelisma;

import java.util.ArrayList;

public class Orders {
    String orderNumber;
    String orderDate;
    String orderTime;
    String orderStatus;
    int orderTotalItems;
    double orderTotalAmount;

    ArrayList<SelectedItems> selectedItem;

    public Orders(String orderNumber, String orderDate, String orderTime,
                  String orderStatus, int orderTotalItems, double orderTotalAmount,
                  ArrayList<SelectedItems> selectedItem) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItem = selectedItem;
    }
}
