package com.module.dot.model;

/*
 Created by Wiscarlens Lucius on 3 JUne 2023.
 */

public class Item {
    private String globalID;
    private Long localID;
    private String creatorID;
    private String imagePath;
    private String name;
    private Double price;
    private String category;
    private String sku;
    private String unitType;
    private int stock;
    private double wholesalePrice;
    private Double tax;
    private String description;

    private Long quantity = 1L;

    public Item() {


    }

    // Use for add item to database
    public Item(String name, double price, String category, String sku,
                String unitType, int stock, double wholesalePrice, double tax, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.sku = sku;
        this.unitType = unitType;
        this.stock = stock;
        this.wholesalePrice = wholesalePrice;
        this.tax = tax;
        this.description = description;
    }

    // Use for display item in the gridview
    public Item(long localID, String globalID, String imagePath, String name,  double price, Double tax, String SKU, String unitType) {
        this.localID = localID;
        this.globalID = globalID;
        this.imagePath = imagePath;
        this.name = name;
        this.price = price;
        this.tax = tax;
        this.sku = SKU;
        this.unitType = unitType;
    }

    // Selected Item
    public Item(String globalID, String name, double price, Double tax, String SKU, Long quantity) {
        this.globalID = globalID;
        this.name = name;
        this.price = price;
        this.tax = tax;
        this.sku = SKU;
        this.quantity = quantity;
    }

    // Use for display order item
    public Item(String globalID, String imagePath, String name, double price, Long quantity) {
        this.globalID = globalID;
        this.imagePath = imagePath;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Item(String globalID, Double price, Long quantity) {
        this.globalID = globalID;
        this.price = price;
        this.quantity = quantity;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public Long getLocalID() {
        return localID;
    }

    public void setLocalID(Long localID) {
        this.localID = localID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
