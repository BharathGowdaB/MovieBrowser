package com.project.moviebrowser.model;

import android.net.Uri;

import com.project.moviebrowser.services.StreamService;

public class TVShowEpisode {
    private int Number;
    private String Name;

    private StreamService Streamer;

    public TVShowEpisode(int number, String name, StreamService service){
        Number = number;
        Name = name;
        Streamer = service;
    }

    public int getNumber() {
        return Number;
    }

    public String getName() {
        return Name;
    }
    public StreamService getStreamer() {
        return Streamer;
    }
}
