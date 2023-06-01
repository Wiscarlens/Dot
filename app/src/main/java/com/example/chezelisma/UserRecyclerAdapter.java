package com.example.chezelisma;

/*
 Created by Wiscarlens Lucius on 1 February 2023.
 */

import android.content.Context;
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
    private ArrayList<String> fullname;
    private ArrayList<String> positionList;
    private ArrayList<Integer> image;

    private Context context;

    public UserRecyclerAdapter(ArrayList<String> fullname, ArrayList<String> position, ArrayList<Integer> image, Context context) {
        this.fullname = fullname;
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
        return fullname.size();
    }

    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.fullNameTextView.setText(fullname.get(position));
        holder.positionTextView.setText(positionList.get(position));
        holder.photoProfile.setImageResource(image.get(position));

        holder.cardView.setOnClickListener(v -> {
            Toast.makeText(context, "You selected " + fullname.get(position), Toast.LENGTH_SHORT).show();
        });
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
