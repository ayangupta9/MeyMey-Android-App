package com.ayandev.meymey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

public class MemeScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title;
    private TextView subreddit;
    private TextView author;
    private AppCompatImageButton floatingActionButton;
    private Intent intent;
    private ProgressBar progressIndicator;
    private MemeModel receivedMemeModel;
    private AppCompatImageButton liked;
    private boolean saveItem = false;
    private FavouriteMemesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        imageView = findViewById(R.id.imageview_meme);
        title = findViewById(R.id.textview_title);
        subreddit = findViewById(R.id.textview_subreddit);
        author = findViewById(R.id.textview_author);
        liked = findViewById(R.id.liked_button);
        floatingActionButton = findViewById(R.id.send_button);
        progressIndicator = findViewById(R.id.progressBar2);
        intent = getIntent();

        viewModel = new ViewModelProvider(this).get(FavouriteMemesViewModel.class);
        buildInit();
    }

    @SuppressLint("SetTextI18n")
    private void buildInit() {
        receivedMemeModel = (MemeModel) intent.getSerializableExtra("currentMeme");
        Glide.with(this).load(receivedMemeModel.getUrl()).into(imageView);
        title.setText("TITLE: " + receivedMemeModel.getTitle());
        author.setText("AUTHOR: u/" + receivedMemeModel.getAuthor());
        subreddit.setText("SUBREDDIT: r/" + receivedMemeModel.getSubreddit());

        saveItem = viewModel.memeExists(receivedMemeModel.getUrl());

        if (saveItem) {
            liked.setImageResource(R.drawable.fav_fill);
        } else {
            liked.setImageResource(R.drawable.fav_border);
        }

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveItem) {
                    liked.setImageResource(R.drawable.fav_border);
                    saveItem = false;

                } else {
                    liked.setImageResource(R.drawable.fav_fill);
                    saveItem = true;
                }

                if (receivedMemeModel.isSaved() && !saveItem) {
                    viewModel.deleteMeme(receivedMemeModel);
                } else if (!receivedMemeModel.isSaved() && saveItem) {
                    viewModel.insertMeme(receivedMemeModel);
                }
                receivedMemeModel.setSaved(saveItem);

            }
        });

        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressIndicator.setVisibility(View.VISIBLE);
                shareMeme();
            }
        });
    }

    public void shareMeme() {
        if (receivedMemeModel.getUrl().endsWith(".gif")) {
            shareMemeAsGif();
        } else {
            shareMemeAsImage();
        }
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    private void shareMemeAsGif() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Meme by " + receivedMemeModel.getAuthor() + "\nVia: MeyMey, by ayangupta9\n" + receivedMemeModel.getUrl());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share this meme"));
    }

    private void shareMemeAsImage() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, " Check out this meme!" + System.currentTimeMillis(), null);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Meme by " + receivedMemeModel.getAuthor() + "\n" + "Via: MeyMey, by ayangupta9");
                Uri memeImage = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, memeImage);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share image by: "));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(receivedMemeModel.getUrl()).into(target);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}