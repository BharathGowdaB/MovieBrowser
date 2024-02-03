package com.project.moviebrowser.model;

import android.net.Uri;

public class TVShowEpisode {
    private int Number;
    private String Name;


    private Uri StreamUri;

    public TVShowEpisode(int number, String name, Uri uri){
        Number = number;
        Name = name;
        StreamUri = uri;
    }

    public int getNumber() {
        return Number;
    }

    public String getName() {
        return Name;
    }
    public Uri getStreamUri() {
        return StreamUri;
    }
}
