package com.project.moviebrowser.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.moviebrowser.R;
import com.project.moviebrowser.adapter.EpisodeAdapter;
import com.project.moviebrowser.adapter.RecommendationAdapter;
import com.project.moviebrowser.adapter.TrailerAdapter;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.model.ModelTV;
import com.project.moviebrowser.model.ModelTrailer;
import com.project.moviebrowser.model.TVShowEpisode;
import com.project.moviebrowser.model.TVShowSeason;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.realm.RealmHelper;
import com.project.moviebrowser.services.StreamService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import io.realm.RealmObject;

public class DetailTelevisionActivity extends AppCompatActivity implements RecommendationAdapter.openRecommendation {

    Toolbar toolbar;
    TextView tvTitle, tvName, tvRating, tvRelease, tvPopularity, tvOverview;
    ImageView imgCover, imgPhoto;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String shareURL;
    int Id;
    ModelTV modelTV;
    ProgressDialog progressDialog;
    RealmHelper helper;
    WebView webStreamer;
    StreamService streamService;
    RecyclerView rvRecd;
    private RecommendationAdapter recdAdapter;
    List<RealmObject> recdTvshows = new ArrayList<>();
    RecyclerView rvEpisode;
    Spinner seasonSpinner;
    TVShowSeason[] Seasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

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
        tvTitle = findViewById(R.id.title);
        tvName = findViewById(R.id.name);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.releaseDate);
        tvPopularity = findViewById(R.id.popularity);
        tvOverview = findViewById(R.id.overview);
        fabShare = findViewById(R.id.fabShare);

        helper = new RealmHelper(this);

        webStreamer = findViewById(R.id.webStreamer);
        webStreamer.getSettings().setJavaScriptEnabled(true);
        webStreamer.setWebChromeClient(StreamService.createWebClient(this));
        webStreamer.setWebViewClient(StreamService.createWebViewClient());

        seasonSpinner = findViewById(R.id.seasonList);
        seasonSpinner.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.videoListTitle)).setText("Episodes");

        rvEpisode = findViewById(R.id.rvTrailer);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rvEpisode.setLayoutManager(layoutManager);
        rvRecd = findViewById(R.id.recommendations);


        if (getIntent().hasExtra("detailTV")) {
            modelTV = (ModelTV) getIntent().getSerializableExtra("detailTV");
        }

        if (modelTV != null) {
            Id = modelTV.getId();

            tvTitle.setText(modelTV.getTitle());
            tvName.setText(modelTV.getTitle());
            String ratingText = String.valueOf(modelTV.getVoteAverage()).substring(0,4) + "/10";
            tvRating.setText(ratingText);
            tvRelease.setText(modelTV.getFirstAirDate());
            tvPopularity.setText(modelTV.getPopularity());
            tvOverview.setText(modelTV.getOverview());
            tvTitle.setSelected(true);
            tvName.setSelected(true);

            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(modelTV.getRating());

            Glide.with(this)
                    .load(modelTV.getBackdropPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(modelTV.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            streamService = new StreamService(Id, false);
            loadStreamer(streamService);

            List<String> seasonNames = new ArrayList<>();
            for(TVShowSeason season: modelTV.getSeasons()) seasonNames.add(season.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.season_list, seasonNames );
            adapter.setDropDownViewResource(R.layout.season_list);
            seasonSpinner.setAdapter(adapter);
            seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    changeSeason(seasonSpinner.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            rvRecd.setHasFixedSize(true);
            rvRecd.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

            getRecommendations();
        }

        if (helper.isFavoriteTV(Id)) imgFavorite.setFavorite(true);
        imgFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Id = modelTV.getId();
                            helper.addFavoriteTV(modelTV);
                            Snackbar.make(buttonView, modelTV.getTitle() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            helper.deleteFavoriteTV(modelTV.getId());
                            Snackbar.make(buttonView, modelTV.getTitle() + " Removed from Favorite",
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
                String subject = modelTV.getTitle();
                String description = modelTV.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + shareURL);
                startActivity(Intent.createChooser(shareIntent, "Share with :"));
            }
        });


    }

    private void getRecommendations() {
        progressDialog.show();
        String url = ApiEndpoint.MOVIEBROWSER_API + "tvshow/id/" + Id;

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray recommendations = response.getJSONArray("recommendations");
                            HashSet<Integer> set = new HashSet<>();
                            recdTvshows = new ArrayList<>();
                            for (int i = 0; i < recommendations.length(); i++) {
                                JSONObject jsonObject = recommendations.getJSONObject(i);
                                ModelTV dataApi = ModelTV.createModel(jsonObject);
                                if(!set.contains(dataApi.getId()) && dataApi.getId() != Id){
                                    set.add(dataApi.getId());
                                    recdTvshows.add(dataApi);
                                }
                            }
                            showRecommendations();
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTelevisionActivity.this, "Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailTelevisionActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRecommendations() {
        recdAdapter = new RecommendationAdapter(this, recdTvshows, this, false);
        rvRecd.setAdapter(recdAdapter);
        recdAdapter.notifyDataSetChanged();
    }

    private void changeSeason(String seasonName){
        for(int k = 0 ; k < modelTV.getSeasons().size() ; k++){
            if(modelTV.getSeasons().get(k).getName().equals(seasonName)){
                TVShowSeason selectedSeason = modelTV.getSeasons().get(k);

                if(selectedSeason.getOverview().length() > 0) tvOverview.setText(selectedSeason.getOverview());
                if(selectedSeason.getAirDate().length() > 0) tvRelease.setText(selectedSeason.getAirDate());
                if(selectedSeason.getPosterPath().length() > 0 && !Objects.equals(selectedSeason.getPosterPath(), "null")){
                    Glide.with(this)
                            .load(selectedSeason.getPosterPath())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_image)
                                    .transform(new RoundedCorners(16)))
                            .into(imgPhoto);
                }

                loadStreamer(new StreamService(Id, selectedSeason.getSeasonNumber()));
                getEpisodes(selectedSeason.getSeasonNumber(), selectedSeason.getEpisodeCount());
                break;
            }
        }
    }

    private void loadStreamer(StreamService streamService){
        webStreamer.loadData(streamService.generateStreamHTML(), "text/html", "UTF-8");
    }

    private void getEpisodes(int seasonNumber, int episodeCount){
        List<TVShowEpisode> episodes = new ArrayList<>();
        for (int i = 1; i <= episodeCount; i++) {
            episodes.add(new TVShowEpisode(i, String.valueOf(i), Id, seasonNumber));
        }

        // Set the adapter on the ListView
        rvEpisode.setAdapter(new EpisodeAdapter(episodes, webStreamer));
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
        Intent intent = new Intent(this, DetailTelevisionActivity.class);
        intent.putExtra("detailTV", (ModelTV) model);
        startActivity(intent);
    }

    public void openURL(View view) {
        Uri uri = Uri.parse("https://vidsrc.to/embed/tv/" + this.Id );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
