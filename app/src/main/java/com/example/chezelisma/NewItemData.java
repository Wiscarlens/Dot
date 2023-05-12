package com.example.chezelisma;

public class NewItemData {
    private String name;
    private String category;
    private String options;
    private String sku;
    private String unitType;
    private String price;

    public NewItemData(String name, String category, String options, String sku, String unitType, String price) {
        this.name = name;
        this.category = category;
        this.options = options;
        this.sku = sku;
        this.unitType = unitType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
