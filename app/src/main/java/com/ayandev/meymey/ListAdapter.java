package com.ayandev.meymey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AyanListAdapter";
    List<MemeModel> stringList;
    Context context;
    private int listLayout;
    private final FavouriteMemesViewModel viewModel;


    public ListAdapter(List<MemeModel> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
        this.listLayout = App.getSavedPrefs(context).getInt("listLayout", 0);
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FavouriteMemesViewModel.class);
    }

    public void setListLayout(int layout) {
        this.listLayout = layout;
        SharedPreferences.Editor editor = App.getSharedPrefEditor(context);
        editor.putInt("listLayout", layout);
        editor.apply();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.listLayout == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
            return new ListViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MemeModel[] currentMeme = {stringList.get(position)};
        if (this.listLayout == 0) {
            if (holder instanceof ListViewHolder) {
                ListViewHolder listViewHolder = (ListViewHolder) holder;

//                Setting up images and title
                Glide.with(this.context).load(currentMeme[0].getUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                listViewHolder.imageView.setImageResource(R.drawable.could_not_load);
                                listViewHolder.textView.setText("Could not load image,list");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                listViewHolder.textView.setText(currentMeme[0].getTitle());
                                return false;
                            }
                        })
                        .into(listViewHolder.imageView);

//              implementing holder click listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewModel.memeExists(currentMeme[0].getUrl())) {
                            currentMeme[0] = viewModel.fetchSingleMeme(currentMeme[0].getUrl());
                        }
                        Intent intent = new Intent(context, MemeScreenActivity.class);
                        intent.putExtra("currentMeme", currentMeme[0]);
                        context.startActivity(intent);
                    }
                });
            }
        } else if (this.listLayout == 1) {
            if (holder instanceof GridViewHolder) {
                GridViewHolder gridViewHolder = (GridViewHolder) holder;

//                Setting up images and title
                Glide.with(this.context).load(currentMeme[0].getUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                gridViewHolder.imageView.setImageResource(R.drawable.could_not_load);
                                gridViewHolder.textView.setText("Could not load image,list");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                gridViewHolder.textView.setText(currentMeme[0].getTitle());
                                return false;
                            }
                        })
                        .into(gridViewHolder.imageView);

//              implementing holder click listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MemeScreenActivity.class);
                        intent.putExtra("currentMeme", currentMeme[0]);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void setAdapterList(ArrayList<MemeModel> list) {
        this.stringList = list;
        notifyDataSetChanged();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tile_meme_image);
            textView = itemView.findViewById(R.id.tile_meme_title);
        }
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_in_grid_item);
            textView = itemView.findViewById(R.id.text_in_grid_item);
        }
    }
}
