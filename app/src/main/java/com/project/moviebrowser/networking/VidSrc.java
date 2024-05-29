package com.project.moviebrowser.networking;

public class VidSrc {
    public static String STREAMER_BASEURL = "https://vidsrc.to/embed/";
    public static String LATEST_MOVIE = "https://vidsrc.to/vapi/movie/add/{page}";
    public static String LATEST_TVSHOW = "https://vidsrc.to/vapi/tv/add/{page}";

    public static String getStreamMovieAPI(int id) {
        return STREAMER_BASEURL + "movie/" + id;
    }
    public static String getStreamTvAPI(int id){
        return STREAMER_BASEURL + "tv/" + id;
    }

    public static String getStreamTvAPI(int id, int season){
        return  STREAMER_BASEURL + "tv/" + id + "/" + season;
    }

    public static String getStreamTvAPI(int id, int season, int episode){
        return STREAMER_BASEURL + "tv/" + id + "/" + season + "/" + episode;
    }

}
