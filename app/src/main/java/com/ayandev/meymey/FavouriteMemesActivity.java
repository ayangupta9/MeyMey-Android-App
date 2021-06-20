package com.ayandev.meymey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavouriteMemesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavouritesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_memes);
        Objects.requireNonNull(getSupportActionBar()).hide();

        adapter = new FavouritesListAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        FavouriteMemesViewModel favouriteMemesViewModel = new ViewModelProvider(this).get(FavouriteMemesViewModel.class);

        recyclerView = findViewById(R.id.favourites_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        favouriteMemesViewModel.fetchAllMemes().observe(this, new Observer<List<MemeModel>>() {
            @Override
            public void onChanged(List<MemeModel> memeModels) {
                adapter.setSavedMemesList((ArrayList<MemeModel>) memeModels);
                recyclerView.setItemViewCacheSize(memeModels.size());
            }
        });
    }
}