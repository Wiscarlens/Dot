package com.module.dot.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.utils.FileManager;
import com.module.dot.utils.LocalFormat;
import com.module.dot.R;
import com.module.dot.model.Item;
import com.module.dot.view.fragments.HomeFragment;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final ArrayList<Item> items;
    private final Context context;
    private HomeFragment homeFragment;

    public ItemAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public ItemAdapter(ArrayList<Item> items, Context context, HomeFragment homeFragment) { // Modify this line
        this.items = items;
        this.context = context;
        this.homeFragment = homeFragment; // Add this line
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Drawable itemImage;

        if (items.get(position).getImagePath() != null) {
            itemImage = FileManager.loadImageLocally(context, "Items", items.get(position).getImagePath());
        } else {
            // Default image
            // TODO: Use category image when item image is not available
            itemImage = ContextCompat.getDrawable(context, R.drawable.baseline_no_image_24);
        }

        holder.ItemImageView.setImageDrawable(itemImage);
        holder.ItemNameTextView.setText(items.get(position).getName());
        holder.priceTextView.setText(LocalFormat.getCurrencyFormat(items.get(position).getPrice()));
        holder.unitTypeTextView.setText((items.get(position).getUnitType()));
        holder.backgroundColor.setBackgroundColor(0); // TODO: Set the background color


        holder.itemLayout.setOnClickListener(v -> {
            if(homeFragment != null) {
                Item selectedItem = new Item(
                        items.get(position).getGlobalID(),
                        items.get(position).getName(),
                        items.get(position).getPrice(),
                        items.get(position).getTax(),
                        items.get(position).getSku(),
                        1L
                );

                // TODO: Optimize - All the line below can be part of addToSElected Item method
                double itemSelectedPrice = items.get(position).getPrice();
                double tax = (items.get(position).getTax() / 100) * itemSelectedPrice;

                homeFragment.totalItem++;

                homeFragment.addToSelectedItems(selectedItem);
                homeFragment.updateTax(tax);
                homeFragment.updateAmount(itemSelectedPrice);
            }

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        ImageView ItemImageView;
        TextView ItemNameTextView;
        TextView priceTextView;
        TextView unitTypeTextView;
        CardView backgroundColor;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLL);
            ItemImageView = itemView.findViewById(R.id.itemImageDesign);
            ItemNameTextView = itemView.findViewById(R.id.itemNameHolderDesign);
            priceTextView = itemView.findViewById(R.id.itemPriceHolderDesign);
            unitTypeTextView = itemView.findViewById(R.id.itemUnitTypeHolderDesign);
            backgroundColor = itemView.findViewById(R.id.ItemBackgroundColor);
        }
    }
}