package com.module.dot.Activities.Items;

/*
 Created by Wiscarlens Lucius on 3 JUne 2023.
 */

import android.graphics.drawable.Drawable;

public class Item {
    private Drawable imagePath;
    private final String name;
    private final double price;
    private String category;
    private String sku;
    private String unitType;
    private int stock;
    private double wholesalePrice;
    private double tax;
    private String description;

    private long id;
    private int frequency = 1;

    // Use for add item to database
    public Item(Drawable imagePath, String name, double price, String category, String sku,
                String unitType, int stock, double wholesalePrice, double tax,
                String description) {
        this.imagePath = imagePath;
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
    public Item(long id, String name, Drawable imagePath, double price, String SKU, String unitType) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.sku = SKU;
        this.unitType = unitType;
    }

    // Selected Item
    public Item(long id, String name, double price, String SKU, int frequency) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sku = SKU;
        this.frequency = frequency;
    }

    // Use for display order item
    public Item(Long id, Drawable imagePath, String name, Double price, Integer frequency) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.price = price;
        this.frequency = frequency;
    }

    public Drawable getImagePath() {
        return imagePath;
    }

    public void setImagePath(Drawable imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}
