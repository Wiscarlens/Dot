package com.module.dot.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.module.dot.utils.FileManager;
import com.module.dot.R;
import com.module.dot.model.Item;

import java.util.ArrayList;

/*
 Created by Wiscarlens Lucius on 15 August 2023.
 */

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.DesignViewHolder> {
    ArrayList<Item> selectedItems;
    Context context;

    public SelectedItemsAdapter(ArrayList<Item> selectedItem, Context context) {
        this.selectedItems = selectedItem;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectedItemsAdapter.DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_items_design, parent, false);
        return new SelectedItemsAdapter.DesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        Drawable itemImage;

        if (selectedItems.get(position).getImagePath() != null) {
            itemImage = FileManager.loadImageLocally(context, "Items", selectedItems.get(position).getGlobalID());
        } else {
            // Default image
            // TODO: Use category image when item image is not available
            itemImage = ContextCompat.getDrawable(context, R.drawable.baseline_no_image_24);
        }

        holder.selectedItem_ImageView.setImageDrawable(itemImage);
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        private final ImageView selectedItem_ImageView;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);

            selectedItem_ImageView = itemView.findViewById(R.id.selectedItemDesign);
        }
    }
}
