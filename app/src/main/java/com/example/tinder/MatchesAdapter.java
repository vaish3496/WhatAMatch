package com.example.tinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> {
    private ArrayList<MatchesItem> mMatchesItems;
    private Context mContext;
    public MatchesAdapter(Context context,ArrayList<MatchesItem> matchesItems){
        mContext = context;
        mMatchesItems = matchesItems;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.matches_item,parent,false);
        MatchesViewHolder mvh = new MatchesViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {
        MatchesItem current = mMatchesItems.get(position);
        Picasso.get().load(current.getmImageURL()).fit().into(holder.mProfileImage);
        holder.mName.setText(current.getmName());
    }

    @Override
    public int getItemCount() {
        return mMatchesItems.size();
    }

    public static class MatchesViewHolder extends RecyclerView.ViewHolder {
        public ImageView mProfileImage;
        public TextView mName;
        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.matches_profile_image);
            mName = itemView.findViewById(R.id.matches_user_name);
        }
    }


}
