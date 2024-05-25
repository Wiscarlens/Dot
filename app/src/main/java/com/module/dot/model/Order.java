package com.module.dot.model;


import com.module.dot.model.Item;

import java.util.ArrayList;

public class Order {
    private String globalID;
    private long orderNumber;
    private String orderDate;
    private String orderTime;
    private String orderStatus;
    private Long orderTotalItems;
    private Double orderTotalAmount;
    private String creatorID;

    private ArrayList<Item> selectedItemList;

    public Order() {
    }

    // Constructor for displaying orders
    public Order(String globalID, long orderNumber, String orderDate, String orderTime, String orderStatus,
                 Long orderTotalItems, Double orderTotalAmount, ArrayList<Item> selectedItemList) {
        this.globalID = globalID;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderTotalItems = orderTotalItems;
        this.orderTotalAmount = orderTotalAmount;
        this.selectedItemList = selectedItemList;
    }

    // Constructor for creating a new order
    public Order(String globalID, String creatorID, String date, String time, Double orderTotalAmount,
                 Long orderTotalItems, String orderStatus) {
        this.globalID = globalID;
        this.creatorID = creatorID;
        this.orderDate = date;
        this.orderTime = time;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
        this.orderTotalItems = orderTotalItems;
    }

    // Constructor for creating a new order
    public Order(String creatorID, String date, String time, double orderTotalAmount,
                 Long orderTotalItems, String orderStatus, ArrayList<Item> selectedItemList) {
        this.creatorID = creatorID;
        this.orderDate = date;
        this.orderTime = time;
        this.orderTotalAmount = orderTotalAmount;
        this.orderTotalItems = orderTotalItems;
        this.orderStatus = orderStatus;
        this.selectedItemList = selectedItemList;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
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

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTotalItems() {
        return orderTotalItems;
    }

    public void setOrderTotalItems(Long orderTotalItems) {
        this.orderTotalItems = orderTotalItems;
    }

    public Double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(Double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
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
