package com.example.chezelisma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ItemGridAdapter extends BaseAdapter {
    private ArrayList<Integer> itemImage;
    private ArrayList<String> itemName;
    private ArrayList<String> itemPrice;
    private ArrayList<String> itemUnitType;
    private ArrayList<Integer> bkgColor;

    private Context context;

    public ItemGridAdapter(ArrayList<Integer> image, ArrayList<String> name, ArrayList<String> price, ArrayList<String> unit,ArrayList<Integer> bkgColor, Context context) {
        this.itemImage = image;
        this.itemName = name;
        this.itemPrice = price;
        this.itemUnitType = unit;
        this.bkgColor = bkgColor;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);

        ImageView ItemImageView;
        TextView ItemNameTextView;
        TextView priceTextView;
        TextView unitTextView;
        CardView backgroundColor;

        ItemImageView = view.findViewById(R.id.itemImageDesign);
        ItemNameTextView = view.findViewById(R.id.itemNameHolderDesign);
        priceTextView = view.findViewById(R.id.itemPriceHolderDesign);
        unitTextView = view.findViewById(R.id.itemUnitTypeHolderDesign);
        backgroundColor = view.findViewById(R.id.ItemBackgroundColor);

        ItemImageView.setImageResource(itemImage.get(position));
        ItemNameTextView.setText(itemName.get(position));
        priceTextView.setText(itemPrice.get(position));
        unitTextView.setText(itemUnitType.get(position));
        backgroundColor.setBackgroundColor(bkgColor.get(position));

        return view;
    }

}
