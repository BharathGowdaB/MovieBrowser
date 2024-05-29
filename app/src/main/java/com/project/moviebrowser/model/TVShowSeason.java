package com.project.moviebrowser.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.realm.RealmObject;

public class TVShowSeason extends RealmObject implements Serializable {
    private int Id;
    private String Name;
    private int SeasonNumber;

    private String AirDate;
    private String Overview;
    private String PosterPath;
    private int EpisodeCount;
    private double VoteAverage;
    private float Rating;

    public static TVShowSeason createModel(JSONObject jsonObject) throws JSONException {
        TVShowSeason model = new TVShowSeason();
        return updateModel(model, jsonObject);
    }

    public  static TVShowSeason updateModel(TVShowSeason model, JSONObject json)  throws JSONException {
        model.setId(json.getInt("id"));
        model.setName(json.getString("name"));
        model.setSeasonNumber(json.getInt("number"));
        model.setAirDate(json.getString("airDate"));
        model.setOverview(json.getString("overview"));
        model.setPosterPath(json.getString("posterPath"));
        model.setEpisodeCount(json.getInt("episodeCount"));
        model.setVoteAverage(json.getDouble("voteAverage"));
        model.setRating((float)json.getDouble("rating"));

        return model;
    }

    public JSONObject toJSONObject() throws JSONException{
        JSONObject object = new JSONObject();
        object.put("id", Id);
        object.put("name", Name);
        object.put("number", SeasonNumber);
        object.put("airDate", AirDate);
        object.put("overview", Overview);
        object.put("posterPath", PosterPath);
        object.put("episodeCount", EpisodeCount);
        object.put("voteAverage", VoteAverage);
        object.put("rating", Rating);

        return object;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSeasonNumber() {
        return SeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        SeasonNumber = seasonNumber;
    }

    public int getEpisodeCount() {
        return EpisodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        EpisodeCount = episodeCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public String getAirDate() {
        return AirDate;
    }

    public void setAirDate(String airDate) {
        AirDate = airDate;
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
}