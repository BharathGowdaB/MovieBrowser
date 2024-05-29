package com.project.moviebrowser.model;

import android.util.Log;
import android.widget.Toast;

import com.project.moviebrowser.networking.ApiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;


public class ModelMovie extends RealmObject implements Serializable {
    public int Id;
    public String Language;
    private String Title;
    private int VoteCount;
    private double VoteAverage;
    private float Rating;
    private String Overview;
    private String ReleaseDate;
    private String PosterPath;
    private String BackdropPath;
    private String Popularity;
    private int RunTime;
    private String TagLine;
    private String Certification;
    private boolean HasTrailer;
    private boolean StreamAvailable;

    private String Videos;

    public ModelMovie() {
    }

    public static ModelMovie createModel(JSONObject jsonObject) throws JSONException, ParseException {
        ModelMovie model = new ModelMovie();
        return updateModel(model, jsonObject);
    }

    public static ModelMovie updateModel(ModelMovie model, JSONObject jsonObject) throws JSONException, ParseException {
        model.setId(jsonObject.getInt("id"));
        model.setTitle(jsonObject.getString("title"));
        model.setLanguage(jsonObject.getString("language"));
        model.setOverview(jsonObject.getString("overview"));
        model.setVoteCount(jsonObject.getInt("voteCount"));
        model.setVoteAverage(jsonObject.getDouble("voteAverage"));
        model.setRating((float)jsonObject.getDouble("rating"));
        model.setPopularity(jsonObject.getString("popularity"));
        model.setPosterPath(jsonObject.getString("posterPath"));
        model.setBackdropPath(jsonObject.getString("backdropPath"));
        model.setRunTime(jsonObject.getInt("runtime"));
        model.setTagLine(jsonObject.getString("tagline"));
        model.setCertification(jsonObject.getString("certification"));
        model.setHasTrailer(jsonObject.getBoolean("hasTrailer"));
        model.setStreamAvailable(jsonObject.getBoolean("streamAvailable"));

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy", Locale.US);
        SimpleDateFormat dateFormat = ApiEndpoint.dateFormat;
        String datePost = jsonObject.getString("releaseDate");
        if(datePost.length() > 0 && dateFormat.parse(datePost) != null)  {
            model.setReleaseDate(formatter.format(Objects.requireNonNull(dateFormat.parse(datePost))));
        }

        List<ModelTrailer> list = new ArrayList<>();
        JSONArray videosArray = jsonObject.getJSONArray("videos");

        for(int i=0; i < videosArray.length(); i++){
            JSONObject video = videosArray.getJSONObject(i);
            list.add(ModelTrailer.createModel(video));
        }

        model.setVideos(list);

        return model;
    }

    public List<ModelTrailer> getVideos() {
        List<ModelTrailer> modelTrailers = new ArrayList<>();
        try {
            JSONArray videosArray = new JSONArray(Videos);
            for(int i=0; i < videosArray.length(); i++){
                JSONObject video = videosArray.getJSONObject(i);
                modelTrailers.add(ModelTrailer.createModel(video));
            }
        } catch (JSONException ignored){ }
        return modelTrailers;
    }

    public void setVideos(List<ModelTrailer> videos) {
        JSONArray array = new JSONArray();
        try {
            for(int i=0; i < videos.size(); i++){
                array.put(i, videos.get(i).toJSONObject());
            }
        } catch (JSONException ignored){ }

        this.Videos = array.toString();
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        VoteAverage = voteAverage;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    public String getPopularity() {
        return Popularity;
    }

    public void setPopularity(String popularity) {
        Popularity = popularity;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public int getVoteCount() {
        return VoteCount;
    }

    public void setVoteCount(int voteCount) {
        VoteCount = voteCount;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public int getRunTime() {
        return RunTime;
    }

    public void setRunTime(int runTime) {
        RunTime = runTime;
    }

    public String getTagLine() {
        return TagLine;
    }

    public void setTagLine(String tagLine) {
        TagLine = tagLine;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }
    public boolean isHasTrailer() {
        return HasTrailer;
    }

    public void setHasTrailer(boolean HasTrailer) {
        this.HasTrailer = HasTrailer;
    }

    public boolean isStreamAvailable() {
        return StreamAvailable;
    }

    public void setStreamAvailable(boolean StreamAvailable) {
        this.StreamAvailable = StreamAvailable;
    }

}
