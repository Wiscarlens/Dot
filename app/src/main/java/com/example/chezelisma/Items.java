package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 3 JUne 2023.
 */

import android.graphics.drawable.Drawable;

public class Items {
    private Drawable image;
    private String name;
    private Double price;
    private String category;
    private String sku;
    private String unitType;
    private Integer stock;
    private Double wholesalePrice;
    private String tax;
    private String description;

    private final Long id;
    private Integer frequency = 1;
    private  Integer backgroundColor;


    public Items(Drawable image, String name, Double price, String category, String sku,
                 String type, Integer stock, Double wholesalePrice, String tax,
                 String description, Long id, Integer frequency) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.category = category;
        this.sku = sku;
        this.unitType = type;
        this.stock = stock;
        this.wholesalePrice = wholesalePrice;
        this.tax = tax;
        this.description = description;
        this.id = id;
        this.frequency = frequency;
    }

    // Use for display item in the gridview
    public Items(Long id, String name, Drawable image, Double price, String SKU, String unitType, Integer backgroundColor) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.sku = SKU;
        this.unitType = unitType;
        this.backgroundColor = backgroundColor;
    }

    public Items(Long id, Drawable image, String name, Double price, String sku, String unitType, Integer backgroundColor) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.unitType = unitType;
        this.backgroundColor = backgroundColor;
    }

    // Selected Items
    public Items(Long id, String name, Double price, String SKU, Integer frequency) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sku = SKU;
        this.frequency = frequency;
    }

    public Items(Long id, Drawable image) {
        this.id = id;
        this.image = image;
    }

    public Items(Long id, Drawable image, String name, Double price, Integer frequency) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.frequency = frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public Double getPrice() {
        return price;
    }

    public Drawable getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getSku() {
        return sku;
    }

    public String getUnitType() {
        return unitType;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getWholesalePrice() {
        return wholesalePrice;
    }

    public String getTax() {
        return tax;
    }

    public long getId() {
        return id;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public String getDescription() {
        return description;
    }
}
