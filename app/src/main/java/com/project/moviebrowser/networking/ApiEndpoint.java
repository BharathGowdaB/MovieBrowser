package com.project.moviebrowser.networking;

public class ApiEndpoint {

    public static String BASEURL = "http://api.themoviedb.org/3/";
    public static String APIKEY = "api_key=c7bbab9fe9f49e2b47a570c1b6c591fb";
    public static String LANGUAGE = "&language=en-US";
    public static String SEARCH_MOVIE = "search/movie?";
    public static String SEARCH_TV = "search/tv?";
    public static String QUERY = "&query=";
    public static String MOVIE_POPULAR = "discover/movie?";
    public static String TV_POPULAR = "discover/tv?";
    public static String URLIMAGE = "https://image.tmdb.org/t/p/w780/";
    public static String URLFILM = "https://www.themoviedb.org/movie/";
    public static String MOVIE_VIDEO = "movie/{id}/videos?";
    public static String TV_VIDEO = "tv/{id}/videos?";

    public static String MOVIE_DETAIL = "movie/{id}?";
    public static String TV_DETAILS = "tv/{id}?";


    public static String STREAMER_BASEURL = "https://vidsrc.to/embed/";
    public static String getStreamMovieAPI(int id) {
        return STREAMER_BASEURL + "movie/" + id;
    }
    public static String getStreamTvAPI(int id){
        return getStreamTvAPI(id, 1, 1);
    }

    public static String getStreamTvAPI(int id, int season){
        return getStreamTvAPI(id, season, 1);

    }

    public static String getStreamTvAPI(int id, int season, int episode){
        return STREAMER_BASEURL + "tv/" + id + "/" + season + "/" + episode;
    }
}
