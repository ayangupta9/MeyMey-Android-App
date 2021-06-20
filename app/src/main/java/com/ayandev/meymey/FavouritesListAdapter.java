 package com.ayandev.meymey;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.FavouritesViewHolder> {

    private List<MemeModel> savedMemesList = new ArrayList<>();
    private final Context context;

    public FavouritesListAdapter(Context context) {
        this.context = context;
    }

    public void setSavedMemesList(ArrayList<MemeModel> list) {
        this.savedMemesList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_meme_list_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesListAdapter.FavouritesViewHolder holder, int position) {
        MemeModel memeModel = savedMemesList.get(position);

//      Setting up views
        Glide.with(this.context).
                load(memeModel.getUrl())
                .error(R.drawable.could_not_load)
                .placeholder(R.drawable.could_not_load)
                .dontAnimate()
                .into(holder.imageView);

        holder.textView.setText(memeModel.getTitle());

//      holder click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MemeScreenActivity.class);
                intent.putExtra("currentMeme", memeModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedMemesList.size();
    }

    protected static class FavouritesViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView textView;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.favourites_meme_image);
            textView = itemView.findViewById(R.id.fav_meme_title);
        }
    }
}
