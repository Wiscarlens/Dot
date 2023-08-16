package com.example.chezelisma;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 Created by Wiscarlens Lucius on 15 August 2023.
 */

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.DesignViewHolder> {
    private final ArrayList<Integer> selectedItemImage;
    private final Context context;

    public SelectedItemsAdapter(ArrayList<Integer> selectedItem, Context context) {
        this.selectedItemImage = selectedItem;
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
       // holder.item_image.setImageDrawable(selectedItemImage.get(position));
        holder.item_image.setImageResource(selectedItemImage.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedItemImage.size();
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        private final ImageView item_image;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.selectedItemDesign);
        }
    }
}
