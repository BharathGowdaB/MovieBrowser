package com.project.moviebrowser.model;

import android.net.Uri;

import com.project.moviebrowser.services.StreamService;

import java.io.Serializable;

import io.realm.RealmObject;

public class TVShowEpisode extends RealmObject implements Serializable {
    private int Number;
    private int ShowId;
    private int SeasonNumber;
    private String Name;
    public TVShowEpisode(){}
    public TVShowEpisode(int number, String name, int showId, int seasonNumber){
        Number = number;
        Name = name;
        ShowId = showId;
        SeasonNumber = seasonNumber;
    }

    public int getNumber() {
        return Number;
    }

    public String getName() {
        return Name;
    }

    public int getShowId() {
        return ShowId;
    }
    public int getSeasonNumber() {
        return SeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        SeasonNumber = seasonNumber;
    }

}
