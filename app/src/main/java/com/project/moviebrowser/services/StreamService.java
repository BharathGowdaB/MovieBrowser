package com.project.moviebrowser.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.project.moviebrowser.networking.ApiEndpoint;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StreamService {
    public boolean isTVShow = false;
    private final String streamAPIEndpoint;

    public StreamService(boolean isTVShow, int id){
        streamAPIEndpoint = isTVShow ? ApiEndpoint.getStreamTvAPI(id) : ApiEndpoint.getStreamMovieAPI(id);
        this.isTVShow = isTVShow;
    }

    public StreamService(int tvId, int season){
        streamAPIEndpoint = ApiEndpoint.getStreamTvAPI(tvId, season);
        this.isTVShow = true;
    }

    public StreamService(int tvId, int season, int episode){
        streamAPIEndpoint = ApiEndpoint.getStreamTvAPI(tvId, season, episode);
        this.isTVShow = true;
    }

    public Uri getStreamUri(){
        return Uri.parse(this.streamAPIEndpoint);
    }

    public boolean hasStreamableService() {
        return StreamService.hasStreamableService(this.streamAPIEndpoint);
    }

    public static boolean hasStreamableService(String uri){
        final boolean[] result = new boolean[1];
        final Handler handler = new Handler(Looper.getMainLooper());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(uri)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    result[0] = response.code() == 200;
                } catch (IOException e) {
                    e.printStackTrace();
                    result[0] = false;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Use result[0] here
                    }
                });
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

}
