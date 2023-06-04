package com.example.chezelisma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 Created by Wiscarlens Lucius on 3 JUne 2023.
 */

public class SelectedProduct {
    public static Map<String, Integer> getProductFrequency(ArrayList<String> itemNames) {
        Map<String, Integer> frequencyName = new HashMap<>();

        // Iterate through each item name
        for (String itemName : itemNames) {
            // If the item name is already in the map, increment its frequency
            if (frequencyName.containsKey(itemName)) {
                int frequency = frequencyName.get(itemName);
                frequencyName.put(itemName, frequency + 1);
            }
            // Otherwise, add the item name to the map with a frequency of 1
            else {
                frequencyName.put(itemName, 1);
            }
        }

        return frequencyName;
    }

    // Combine name and price and remove duplicate
    public static Map<String, Double> combinePriceName(ArrayList<String> itemNames, ArrayList<Double> itemPrices) {
        Map<String, Double> combinedPriceName = new HashMap<>();

        for (int i = 0; i < itemNames.size(); i++) {
            String name = itemNames.get(i);
            double price = itemPrices.get(i);

            if (combinedPriceName.containsKey(name)) {
                // If the item is already present, update the price
                combinedPriceName.put(name, price);
            } else {
                // If the item is not present, add it to the map
                combinedPriceName.put(name, price);
            }
        }

        return combinedPriceName;
    }

    public static Product[] getProductAsArray(Map<String, Integer> nameFrequency, Map<String, Double> namePrice) {
        Set<Map.Entry<String, Integer>> frequencyEntrySet = nameFrequency.entrySet();
        Set<Map.Entry<String, Double>> priceEntrySet = namePrice.entrySet();

        Iterator<Map.Entry<String, Integer>> frequencyIterator = frequencyEntrySet.iterator();
        Iterator<Map.Entry<String, Double>> priceIterator = priceEntrySet.iterator();

        int size = Math.min(frequencyEntrySet.size(), priceEntrySet.size());
        Product[] productArray = new Product[size];

        for (int i = 0; i < size; i++) {
            Map.Entry<String, Integer> frequencyEntry = frequencyIterator.next();
            String name = frequencyEntry.getKey();
            int frequency = frequencyEntry.getValue();

            Map.Entry<String, Double> priceEntry = priceIterator.next();
            Double price = priceEntry.getValue();

            productArray[i] = new Product(name, frequency, price);
        }

        return productArray;
    }

}
