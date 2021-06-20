package com.ayandev.meymey;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final int size = 50;
    String url = "https://meme-api.herokuapp.com/gimme/" + size;
    ArrayList<MemeModel> memeUrl;
    private ListAdapter adapter;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private Toolbar toolbarCustom;
    private ImageView backgroundImageView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView shimmerText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private Button favButton;
    private CardView githubCommunication;
    private CardView googleCommunication;
    private CardView redditCommunication;
    private CardView linkedInCommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).hide();
        memeUrl = new ArrayList<>();

//      Linking View declarations with XMLs
        settingViewsWithIds();

//      Implementing all the listeners and inflaters for necessary views
        viewListenersInflaters();

//        Setting up request queue
        requestQueue = Volley.newRequestQueue(this);

//        Starting loading shimmer
        shimmerFrameLayout.startShimmer();

//        Calling networking function (using volley)
        callAPI();
    }

    private void settingViewsWithIds() {
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        shimmerText = findViewById(R.id.shimmer_text);
        toolbarCustom = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);
        backgroundImageView = findViewById(R.id.background_imageview);
        appBarLayout = findViewById(R.id.appbar);
        favButton = findViewById(R.id.favourites_drawer_tab);
        githubCommunication = findViewById(R.id.github_com);
        googleCommunication = findViewById(R.id.google_com);
        redditCommunication = findViewById(R.id.reddit_com);
        linkedInCommunication = findViewById(R.id.linkedin_com);
    }

    private void viewListenersInflaters() {

        toolbarCustom.inflateMenu(R.menu.more_menu);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setMeasurementCacheEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListAdapter(memeUrl, this);

        recyclerView.setAdapter(adapter);

        if (App.getSavedPrefs(this).getInt("listLayout", 1) == 0) {
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset != 0) {
                    toolbarCustom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    });
                    //                    Log.d(TAG, "onOffsetChanged: Fully Expanded");
                }
            }
        });


        Glide.with(this).asGif().load(R.drawable.gif_background).into(backgroundImageView);

        toolbarCustom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_layout_item:
//                        Toast.makeText(MainActivity.this, "LINEAR LAYOUT", Toast.LENGTH_SHORT).show();
                        adapter.setListLayout(0);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
//                        Log.d(TAG, "onOptionsItemSelected: LINEAR LAYOUT");
                        return true;
                    case R.id.grid_layout_item:
//                        Toast.makeText(MainActivity.this, "GRID LAYOUT", Toast.LENGTH_SHORT).show();
                        adapter.setListLayout(1);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(adapter);
//                        Log.d(TAG, "onOptionsItemSelected: GRID LAYOUT");
                        return true;
                    default:
                        return false;
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarCustom, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drawerIntent = new Intent(MainActivity.this, FavouriteMemesActivity.class);
                startActivity(drawerIntent);
            }
        });


        githubCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/ayangupta9";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                Toast.makeText(MainActivity.this, "GITHUB COMMUNICATION", Toast.LENGTH_SHORT).show();
            }
        });
        googleCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "ayan.gupta.dev@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                try {
                    startActivity(Intent.createChooser(emailIntent, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "APP IS NOT INSTALLED", Toast.LENGTH_SHORT).show();
                }
            }
        });
        redditCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://reddit.com/user/ayan-gupta";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                Toast.makeText(MainActivity.this, "DISCORD COMMUNICATION", Toast.LENGTH_SHORT).show();
            }
        });
        linkedInCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.linkedin.com/in/ayan-gupta-4b2158213";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                Toast.makeText(MainActivity.this, "LINKEDIN COMMUNICATION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callAPI() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<MemeModel>>() {
                    }.getType();
                    memeUrl = gson.fromJson(response.get("memes").toString(), listType);
                    adapter.setAdapterList(memeUrl);
                    recyclerView.setItemViewCacheSize(memeUrl.size());
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shimmerText.setText("ERROR! TRY AGAIN");
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.stop();
        requestQueue = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.more_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}