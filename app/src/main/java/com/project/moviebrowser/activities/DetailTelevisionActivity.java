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
import com.project.moviebrowser.adapter.TrailerAdapter;
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
import java.util.List;
import java.util.Objects;

public class DetailTelevisionActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle, tvName, tvRating, tvRelease, tvPopularity, tvOverview;
    ImageView imgCover, imgPhoto;
    RecyclerView rvEpisodeList;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    Spinner seasonSpinner;
    String NameFilm, ReleaseDate, Popularity, Overview, Cover, Thumbnail, movieURL;
    int Id;
    double Rating;
    TVShowSeason[] Seasons;
    ModelTV modelTV;
    ProgressDialog progressDialog;
    RealmHelper helper;

    WebView webStreamer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

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
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvOverview = findViewById(R.id.tvOverview);
        fabShare = findViewById(R.id.fabShare);
        seasonSpinner = findViewById(R.id.seasonList);
        seasonSpinner.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.videoListTitle)).setText("Episodes");

        rvEpisodeList = findViewById(R.id.rvTrailer);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rvEpisodeList.setLayoutManager(layoutManager);

        helper = new RealmHelper(this);

        webStreamer = findViewById(R.id.webStreamer);
        WebSettings webSettings = webStreamer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webStreamer.setWebChromeClient(StreamService.createWebClient(this));

        modelTV = (ModelTV) getIntent().getSerializableExtra("detailTV");
        if (modelTV != null) {

            Id = modelTV.getId();
            NameFilm = modelTV.getName();
            Rating = modelTV.getVoteAverage();
            ReleaseDate = modelTV.getReleaseDate();
            Popularity = modelTV.getPopularity();
            Overview = modelTV.getOverview();
            Cover = modelTV.getBackdropPath();
            Thumbnail = modelTV.getPosterPath();
            movieURL = ApiEndpoint.URLFILM + "" + Id;

            tvTitle.setText(NameFilm);
            tvName.setText(NameFilm);
            tvRating.setText(Rating + "/10");
            tvRelease.setText(ReleaseDate);
            tvPopularity.setText(Popularity);
            tvOverview.setText(Overview);
            tvTitle.setSelected(true);
            tvName.setSelected(true);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(ApiEndpoint.URLIMAGE + Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            getTVDetails();
        }
        if(helper.isFavoriteTV(Id))
            imgFavorite.setFavorite(true);
        imgFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Id = modelTV.getId();
                            NameFilm = modelTV.getName();
                            Rating = modelTV.getVoteAverage();
                            Overview = modelTV.getOverview();
                            ReleaseDate = modelTV.getReleaseDate();
                            Thumbnail = modelTV.getPosterPath();
                            Cover = modelTV.getBackdropPath();
                            Popularity = modelTV.getPopularity();
                            Seasons = modelTV.getSeasons();
                            helper.addFavoriteTV(Id, NameFilm, Rating, Overview, ReleaseDate, Thumbnail, Cover, Popularity, Seasons);
                            Snackbar.make(buttonView, modelTV.getName() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            helper.deleteFavoriteTV(modelTV.getId());
                            Snackbar.make(buttonView, modelTV.getName() + " Removed from Favorite",
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
                String subject = modelTV.getName();
                String description = modelTV.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + movieURL);
                startActivity(Intent.createChooser(shareIntent, "Share with :"));
            }
        });

    }

    private void getTVDetails(){
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.TV_DETAILS + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray seasonArray = response.getJSONArray("seasons");
                            TVShowSeason[] tvSeasonArray = new TVShowSeason[seasonArray.length()];

                            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

                            List<String> seasonNames = new ArrayList<>();

                            for(int j = 0 ; j < seasonArray.length(); j++){
                                JSONObject season = seasonArray.getJSONObject(j);
                                tvSeasonArray[j] = new TVShowSeason();
                                tvSeasonArray[j].setId(season.getInt("id"));
                                tvSeasonArray[j].setSeasonNumber(season.getInt("season_number"));
                                tvSeasonArray[j].setEpisodeCount(season.getInt("episode_count"));
                                tvSeasonArray[j].setName(season.getString("name"));
                                seasonNames.add(season.getString("name"));
                                tvSeasonArray[j].setOverview(season.getString("overview"));
                                tvSeasonArray[j].setPosterPath(season.getString("poster_path"));

                                String datePost = season.getString("air_date");
                                if(!datePost.equals("null") && dateFormat.parse(datePost) != null)
                                    tvSeasonArray[j].setAirDate(formatter.format(dateFormat.parse(datePost)));
                            }
                            modelTV.setSeasons(tvSeasonArray);

                            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.season_list, seasonNames );
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

                            getEpisodes(tvSeasonArray[0].getSeasonNumber(), tvSeasonArray[0].getEpisodeCount());
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeSeason(String seasonName){
        progressDialog.show();
        for(int k = 0 ; k < modelTV.getSeasons().length ; k++){
            if(modelTV.getSeasons()[k].getName().equals(seasonName)){
                TVShowSeason selectedSeason = modelTV.getSeasons()[k];

                if(selectedSeason.getOverview().length() > 0) tvOverview.setText(selectedSeason.getOverview());
                if(selectedSeason.getAirDate().length() > 0) tvRelease.setText(selectedSeason.getAirDate());
                if(selectedSeason.getPosterPath().length() > 0 && !Objects.equals(selectedSeason.getPosterPath(), "null")){
                    Glide.with(this)
                            .load(ApiEndpoint.URLIMAGE + selectedSeason.getPosterPath())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_image)
                                    .transform(new RoundedCorners(16)))
                            .into(imgPhoto);
                }

                getEpisodes(selectedSeason.getSeasonNumber(), selectedSeason.getEpisodeCount());
                break;
            }
        }
        progressDialog.dismiss();
    }

    private void loadStreamer(StreamService streamService){
        webStreamer.loadData(streamService.generateStreamHTML(), "text/html", "UTF-8");
    }

    private void getEpisodes(int seasonNumber, int episodeCount){
        progressDialog.show();
        List<TVShowEpisode> episodes = new ArrayList<>();
        for (int i = 1; i <= episodeCount; i++) {
            episodes.add(new TVShowEpisode(i, String.valueOf(i), new StreamService(Id, seasonNumber, i)));
        }

        // Set the adapter on the ListView
        rvEpisodeList.setAdapter(new EpisodeAdapter(episodes, webStreamer));
        loadStreamer(episodes.get(0).getStreamer());
        progressDialog.hide();
    }

    /*
    private void getEpisodes(int seasonNumber) {

        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.TV_SEASON_DETAILS + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .addPathParameter("number", String.valueOf(seasonNumber))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            List<TVShowEpisode> episodeList = new ArrayList<>();

                            JSONArray jsonArray = response.getJSONArray("episodes");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StreamService streamService = new StreamService(Id, seasonNumber, jsonObject.getInt("episode_number"));
                                episodeList.add(new TVShowEpisode(jsonObject.getInt("episode_number"), jsonObject.getString("name"), streamService.getStreamUri()));
                            }

                            rvEpisodeList.setAdapter(new EpisodeAdapter(episodeList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTelevisionActivity.this,"Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailTelevisionActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    */

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

    public void openURL(View view) {
        Uri uri = Uri.parse("https://vidsrc.to/embed/tv/" + this.Id + "/1/1");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
