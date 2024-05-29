package com.project.moviebrowser.model;

import com.project.moviebrowser.networking.ApiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModelTV extends RealmObject implements Serializable {

    private int Id;
    private String Title;
    private String Language;
    private String Overview;
    private String FirstAirDate;
    private String LastAirDate;
    private int VoteCount;
    private double VoteAverage;
    private float Rating;
    private String Popularity;
    private String PosterPath;
    private String BackdropPath;
    private RealmList<Genre> Genres;
    private String TagLine;
    private String Certification;
    private String Seasons;
    private TVShowSeason Specials;
    private TVShowEpisode LastEpisodeToAir;
    private TVShowEpisode NextEpisodeToAir;
    private Boolean StreamFirstEpisode;
    private Boolean StreamLastEpisode;

    public ModelTV() {
    }

    public static ModelTV createModel(JSONObject jsonObject) throws JSONException, ParseException {
        ModelTV model = new ModelTV();
        return updateModel(model, jsonObject);
    }

    public static ModelTV updateModel(ModelTV model, JSONObject jsonObject) throws JSONException, ParseException {
        model.setId(jsonObject.getInt("id"));
        model.setTitle(jsonObject.getString("title"));
        model.setLanguage(jsonObject.getString("language"));
        model.setOverview(jsonObject.getString("overview"));
        model.setLastAirDate(jsonObject.getString("lastAirDate"));
        model.setVoteCount(jsonObject.getInt("voteCount"));
        model.setVoteAverage(jsonObject.getDouble("voteAverage"));
        model.setRating((float)jsonObject.getDouble("rating"));
        model.setPopularity(jsonObject.getString("popularity"));
        model.setPosterPath(jsonObject.getString("posterPath"));
        model.setBackdropPath(jsonObject.getString("backdropPath"));
        model.setTagLine(jsonObject.getString("tagline"));
        model.setCertification(jsonObject.getString("certification"));
        model.setStreamFirstEpisode(jsonObject.getBoolean("streamFirstEpisode"));
        model.setStreamLastEpisode(jsonObject.getBoolean("streamLastEpisode"));

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy", Locale.US);
        SimpleDateFormat dateFormat = ApiEndpoint.dateFormat;
        String datePost = jsonObject.getString("firstAirDate");
        if(datePost.length() > 0 && dateFormat.parse(datePost) != null)  {
            model.setFirstAirDate(formatter.format(Objects.requireNonNull(dateFormat.parse(datePost))));
        }

        List<TVShowSeason> list = new ArrayList<>();
        JSONArray seasonsArray = jsonObject.getJSONArray("seasons");

        for(int i=0; i < seasonsArray.length(); i++){
            JSONObject season = seasonsArray.getJSONObject(i);
            list.add(TVShowSeason.createModel(season));
        }

        model.setSeasons(list);

        return model;
    }

    public List<TVShowSeason> getSeasons() {
        List<TVShowSeason> seasonList = new ArrayList<>();
        try {
            JSONArray seasonsArray = new JSONArray(Seasons);
            for(int i=0; i < seasonsArray.length(); i++){
                JSONObject season = seasonsArray.getJSONObject(i);
                seasonList.add(TVShowSeason.createModel(season));
            }
        } catch (JSONException ignored){ }
        return seasonList;
    }

    public void setSeasons(List<TVShowSeason> seasons) {
        JSONArray array = new JSONArray();
        try {
            for(int i=0; i < seasons.size(); i++){
                array.put(i, seasons.get(i).toJSONObject());
            }
        } catch (JSONException ignored){ }

        this.Seasons = array.toString();
    }

    public Genre[] getGenres() {
        Genre[] genres = new Genre[Genres.size()];
        return Genres.toArray(genres);
    }

    public void setGenres(Genre[] genres) {
        RealmList<Genre> genreList = new RealmList<>();
        genreList.addAll(Arrays.asList(genres));
        Genres = genreList;
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

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getFirstAirDate() {
        return FirstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        FirstAirDate = firstAirDate;
    }

    public String getLastAirDate() {
        return LastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        LastAirDate = lastAirDate;
    }

    public int getVoteCount() {
        return VoteCount;
    }

    public void setVoteCount(int voteCount) {
        VoteCount = voteCount;
    }

    public double getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        VoteAverage = voteAverage;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getPopularity() {
        return Popularity;
    }

    public void setPopularity(String popularity) {
        Popularity = popularity;
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


    public TVShowSeason getSpecials() {
        return Specials;
    }

    public void setSpecials(TVShowSeason specials) {
        Specials = specials;
    }

    public TVShowEpisode getLastEpisodeToAir() {
        return LastEpisodeToAir;
    }

    public void setLastEpisodeToAir(TVShowEpisode lastEpisodeToAir) {
        LastEpisodeToAir = lastEpisodeToAir;
    }

    public TVShowEpisode getNextEpisodeToAir() {
        return NextEpisodeToAir;
    }

    public void setNextEpisodeToAir(TVShowEpisode nextEpisodeToAir) {
        NextEpisodeToAir = nextEpisodeToAir;
    }

    public Boolean getStreamFirstEpisode() {
        return StreamFirstEpisode;
    }

    public void setStreamFirstEpisode(Boolean streamFirstEpisode) {
        StreamFirstEpisode = streamFirstEpisode;
    }

    public Boolean getStreamLastEpisode() {
        return StreamLastEpisode;
    }

    public void setStreamLastEpisode(Boolean streamLastEpisode) {
        StreamLastEpisode = streamLastEpisode;
    }
}
