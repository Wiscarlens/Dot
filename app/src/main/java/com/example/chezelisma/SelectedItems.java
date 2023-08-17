package com.example.chezelisma;

public class SelectedItems {
    String itemName;
    int itemImage;
    int frequency;
    double unitPrice;

    public SelectedItems(String itemName, int frequency, double unitPrice) {
        this.itemName = itemName;
        this.frequency = frequency;
        this.unitPrice = unitPrice;
    }

    public SelectedItems(int itemImage) {
        this.itemImage = itemImage;
    }
}
