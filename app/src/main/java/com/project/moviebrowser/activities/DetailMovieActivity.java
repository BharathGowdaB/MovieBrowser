package com.project.moviebrowser.activities;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

import static com.project.moviebrowser.services.StreamService.COVER_SCREEN_PARAMS;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.util.Rational;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.moviebrowser.R;
import com.project.moviebrowser.adapter.MovieAdapter;
import com.project.moviebrowser.adapter.RecommendationAdapter;
import com.project.moviebrowser.adapter.TrailerAdapter;
import com.project.moviebrowser.configuration.Configuration;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.model.ModelTrailer;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.realm.RealmHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.moviebrowser.services.FullscreenHolder;
import com.project.moviebrowser.services.StreamService;
import com.project.moviebrowser.view.TriSelector;

import io.realm.RealmObject;


public class DetailMovieActivity extends AppCompatActivity implements RecommendationAdapter.openRecommendation {
    Toolbar toolbar;
    TextView title, name, rating, releaseDate, popularity, overview;
    ImageView imgCover, imgPhoto;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String shareURL;
    int Id;
    ModelMovie modelMovie;
    ProgressDialog progressDialog;
    RealmHelper helper;
    WebView webStreamer;
    StreamService streamService;
    RecyclerView rvRecd;
    private RecommendationAdapter recdAdapter;
    List<RealmObject> recdMovies = new ArrayList<>();
    RecyclerView rvTrailer;
    List<ModelTrailer> modelTrailer = new ArrayList<>();
    TrailerAdapter trailerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgFavorite = findViewById(R.id.imgFavorite);
        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.tvRating);
        releaseDate = findViewById(R.id.releaseDate);
        popularity = findViewById(R.id.popularity);
        overview = findViewById(R.id.overview);
        fabShare = findViewById(R.id.fabShare);

        helper = new RealmHelper(this);

        webStreamer = findViewById(R.id.webStreamer);
        webStreamer.getSettings().setJavaScriptEnabled(true);
        webStreamer.setWebChromeClient(StreamService.createWebClient(this));
        webStreamer.setWebViewClient(StreamService.createWebViewClient());

        rvTrailer = findViewById(R.id.rvTrailer);
        rvRecd = findViewById(R.id.recommendations);


        if(getIntent().hasExtra("detailMovie")){
            modelMovie = (ModelMovie) getIntent().getSerializableExtra("detailMovie");
        }

        if (modelMovie != null) {
            Id = modelMovie.getId();

            title.setText(modelMovie.getTitle());
            name.setText(modelMovie.getTitle());
            String ratingText = String.valueOf(modelMovie.getVoteAverage()).substring(0,4) + "/10";
            rating.setText(ratingText);
            releaseDate.setText(modelMovie.getReleaseDate());
            popularity.setText(modelMovie.getPopularity());
            overview.setText(modelMovie.getOverview());
            title.setSelected(true);
            name.setSelected(true);

            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(modelMovie.getRating());

            shareURL = ApiEndpoint.URL_MOVIE + Id;

            Glide.with(this)
                    .load(modelMovie.getBackdropPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(modelMovie.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            streamService = new StreamService(Id, true);
            webStreamer.loadData(streamService.generateStreamHTML(), "text/html", "UTF-8");

            rvTrailer.setHasFixedSize(true);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setJustifyContent(JustifyContent.CENTER);
            layoutManager.setAlignItems(AlignItems.CENTER);
            rvTrailer.setLayoutManager(layoutManager);

            modelTrailer = modelMovie.getVideos();
            showTrailer();

            rvRecd.setHasFixedSize(true);
            rvRecd.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

            getRecommendations();
        }


        if(helper.isFavoriteMovie(Id)) imgFavorite.setFavorite(true);
        imgFavorite.setOnFavoriteChangeListener( new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Id = modelMovie.getId();
                            helper.addFavoriteMovie(modelMovie);
                            Snackbar.make(buttonView, modelMovie.getTitle() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            helper.deleteFavoriteMovie(modelMovie.getId());
                            Snackbar.make(buttonView, modelMovie.getTitle() + " Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = modelMovie.getTitle();
                String description = modelMovie.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + shareURL);
                startActivity(Intent.createChooser(shareIntent, "Share with :"));
            }
        });
    }

    private void getRecommendations() {
        progressDialog.show();
        String url = ApiEndpoint.MOVIEBROWSER_API + "movie/id/" + Id;

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray collections = response.getJSONObject("collection").getJSONArray("parts");
                            JSONArray recommendations = response.getJSONArray("recommendations");
                            Set<Integer> set = new HashSet<>();
                            recdMovies = new ArrayList<>();
                            for (int i = 0; i < collections.length(); i++) {
                                JSONObject jsonObject = collections.getJSONObject(i);
                                ModelMovie dataApi = ModelMovie.createModel(jsonObject);
                                if(!set.contains(dataApi.getId()) && dataApi.getId() != Id){
                                    set.add(dataApi.getId());
                                    recdMovies.add(dataApi);
                                }
                            }
                            for (int i = 0; i < recommendations.length(); i++) {
                                JSONObject jsonObject = recommendations.getJSONObject(i);
                                ModelMovie dataApi = ModelMovie.createModel(jsonObject);
                                if(!set.contains(dataApi.getId()) && dataApi.getId() != Id){
                                    set.add(dataApi.getId());
                                    recdMovies.add(dataApi);
                                }
                            }
                            showRecommendations();
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailMovieActivity.this, "Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailMovieActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRecommendations() {
        recdAdapter = new RecommendationAdapter(this, recdMovies, this, true);
        rvRecd.setAdapter(recdAdapter);
        recdAdapter.notifyDataSetChanged();
    }

    private void showTrailer() {
        trailerAdapter = new TrailerAdapter(DetailMovieActivity.this, modelTrailer);
        rvTrailer.setAdapter(trailerAdapter);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openRecommended(RealmObject model) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("detailMovie", (ModelMovie) model);
        startActivity(intent);
    }

}
