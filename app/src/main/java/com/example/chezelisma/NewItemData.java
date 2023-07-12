package com.example.chezelisma;

public class NewItemData {
    private String name;
    private double price;
    private String category;

    private String sku;
    private String unitType;
    private int stock;

    private double wholesalesPrice;
    private double tax;
    private String description;

    //private String options;


//    public NewItemData(String name, String category, String options, String sku, String unitType, String price) {
//        this.name = name;
//        this.category = category;
//        this.options = options;
//        this.sku = sku;
//        this.unitType = unitType;
//        this.price = price;
//    }


    public NewItemData(String name, double price, String category, String sku, String unitType, int stock, double wholesalesPrice, double tax, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.sku = sku;
        this.unitType = unitType;
        this.stock = stock;
        this.wholesalesPrice = wholesalesPrice;
        this.tax = tax;
        this.description = description;
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

    public double getWholesalesPrice() {
        return wholesalesPrice;
    }

    public void setWholesalesPrice(double wholesalesPrice) {
        this.wholesalesPrice = wholesalesPrice;
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
}
