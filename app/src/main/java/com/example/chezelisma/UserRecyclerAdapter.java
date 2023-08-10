package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    private ArrayList<String> fullName;
    private ArrayList<String> positionList;
    private ArrayList<Drawable> image;

    private Context context;

    public UserRecyclerAdapter(ArrayList<String> fullName, ArrayList<String> position, ArrayList<Drawable> image, Context context) {
        this.fullName = fullName;
        this.positionList = position;
        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_design, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return fullName.size();
    }

    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.fullNameTextView.setText(fullName.get(position));
        holder.positionTextView.setText(positionList.get(position));
        holder.photoProfile.setImageDrawable(image.get(position)); // Set the Drawable object

        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "You selected " + fullName.get(position), Toast.LENGTH_SHORT).show());
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTextView;
        private TextView positionTextView;
        private ImageView photoProfile;

        private CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTextView = itemView.findViewById(R.id.UserFullNameDesign);
            positionTextView = itemView.findViewById(R.id.positionDesign);
            photoProfile = itemView.findViewById(R.id.profileImage);
            cardView = itemView.findViewById(R.id.userCardView);
        }
    }
}
