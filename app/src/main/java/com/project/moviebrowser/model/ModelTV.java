package com.project.moviebrowser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModelTV extends RealmObject implements Serializable {

    private int Id;
    private String Name;
    private double VoteAverage;
    private String Overview;
    private String ReleaseDate;
    private String PosterPath;
    private String BackdropPath;
    private String Popularity;

    private RealmList<TVShowSeason> Seasons;

    public ModelTV() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public TVShowSeason[] getSeasons() {
        TVShowSeason[] tvSeasons = new TVShowSeason[Seasons.size()];
        return Seasons.toArray(tvSeasons);

    }

    public void setSeasons(TVShowSeason[] seasons) {
        RealmList<TVShowSeason> tvSeasons = new RealmList<>();
        tvSeasons.addAll(Arrays.asList(seasons));
        Seasons = tvSeasons;
    }
}
