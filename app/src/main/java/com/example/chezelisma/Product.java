package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 3 JUne 2023.
 */

import androidx.annotation.NonNull;

public class Product {
    private String name;
    Integer frequency;
    private Double price;

    public Product(String name, Integer frequency, Double price) {
        this.name = name;
        this.frequency = frequency;
        this.price = price;
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

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", frequency=" + frequency +
                ", price=" + price +
                '}';
    }
}
