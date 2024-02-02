package com.project.moviebrowser.model;

import java.io.Serializable;

import io.realm.RealmObject;

public class TVShowSeason extends RealmObject implements Serializable {
    private int Id;
    private int SeasonNumber;
    private int EpisodeCount;
    private String Name;
    private String Overview;
    private String PosterPath;
    private String AirDate;


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
}
