package com.module.dot.Activities.Users;

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

import com.module.dot.Helpers.Utils;
import com.module.dot.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    private final ArrayList<Users> users_for_display;
    private final Context context;

    public UserRecyclerAdapter(ArrayList<Users> users_for_display, Context context) {
        this.users_for_display = users_for_display;
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
        return users_for_display.size();
    }

    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        if (users_for_display.get(position).getProfileImagePath() == null) {
            holder.initial.setVisibility(View.VISIBLE);
            holder.initial.setText(users_for_display.get(position).getFirstName().substring(0, 1));

        } else {
            holder.initial.setVisibility(View.GONE);

            Drawable defaultProfileImage = Utils.getDrawableFromDrawableFolder(context, R.drawable.cartoon);
            holder.photoProfile.setImageDrawable(defaultProfileImage);

        }


        holder.fullNameTextView.setText(users_for_display.get(position).getFullName());
        holder.positionTextView.setText(users_for_display.get(position).getPosition());

        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "You selected " + users_for_display.get(position).getFullName(), Toast.LENGTH_SHORT).show());
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullNameTextView;
        private final TextView positionTextView;
        private final CircleImageView photoProfile;
        private final TextView initial;
        private final CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTextView = itemView.findViewById(R.id.UserFullNameDesign);
            positionTextView = itemView.findViewById(R.id.positionDesign);
            photoProfile = itemView.findViewById(R.id.profileImage);
            initial = itemView.findViewById(R.id.tv_initials);
            cardView = itemView.findViewById(R.id.userCardView);
        }
    }
}
