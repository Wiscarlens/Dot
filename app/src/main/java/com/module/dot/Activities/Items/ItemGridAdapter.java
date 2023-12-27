package com.module.dot.Activities.Items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.module.dot.Helpers.ImageStorageManager;
import com.module.dot.Helpers.LocalFormat;
import com.module.dot.R;

import java.util.ArrayList;

public class ItemGridAdapter extends BaseAdapter {
    private final ArrayList<Item> items;
    private final Context context;
    public ItemGridAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getLocalID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);

        ImageView ItemImageView = view.findViewById(R.id.itemImageDesign);
        TextView ItemNameTextView = view.findViewById(R.id.itemNameHolderDesign);
        TextView priceTextView = view.findViewById(R.id.itemPriceHolderDesign);
        TextView unitTypeTextView = view.findViewById(R.id.itemUnitTypeHolderDesign);
        CardView backgroundColor = view.findViewById(R.id.ItemBackgroundColor);

        // TODO: Set the image
        if (items.get(position).getImagePath() != null) {
            Drawable itemImage = ImageStorageManager.loadImageLocally(context, "Items", items.get(position).getImagePath());
            ItemImageView.setImageDrawable(itemImage);


        } else {
            // Default image
        }




//        ItemImageView.setImageDrawable(items.get(position).getImagePath());
        ItemImageView.setImageDrawable(null); // Set the Drawable object
        ItemNameTextView.setText(items.get(position).getName());
        priceTextView.setText(LocalFormat.getCurrencyFormat(items.get(position).getPrice()));
        unitTypeTextView.setText((items.get(position).getUnitType()));
        backgroundColor.setBackgroundColor(0); // TDO: Set the background color

        return view;
    }

}
