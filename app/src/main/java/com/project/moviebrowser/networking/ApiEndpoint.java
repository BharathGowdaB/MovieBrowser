package com.project.moviebrowser.networking;

import com.project.moviebrowser.configuration.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApiEndpoint {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static String MOVIEBROWSER_API = "https://82bm2uomlg.execute-api.ap-south-1.amazonaws.com/";
    public static String URL_MOVIE = "https://www.themoviedb.org/movie/";
    public static String URL_TVSHOW = "https://www.themoviedb.org/tv/";
}
