package com.project.moviebrowser.services;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.project.moviebrowser.R;
import com.project.moviebrowser.activities.DetailMovieActivity;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.VidSrc;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StreamService {
    public static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public boolean isMovie = false;
    private final String streamAPIEndpoint;

    public StreamService(int id, boolean isMovie){
        streamAPIEndpoint = isMovie ? VidSrc.getStreamMovieAPI(id) : VidSrc.getStreamTvAPI(id);
        this.isMovie = isMovie;
    }

    public StreamService(int tvId, int season){
        streamAPIEndpoint = VidSrc.getStreamTvAPI(tvId, season);
    }

    public StreamService(int tvId, int season, int episode){
        streamAPIEndpoint = VidSrc.getStreamTvAPI(tvId, season, episode);
    }

    public Uri getStreamUri(){
        return Uri.parse(this.streamAPIEndpoint);
    }

    public boolean hasStreamableService() {
        return StreamService.hasStreamableService(this.streamAPIEndpoint);
    }

    public String generateStreamHTML(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "    <style>\n" +
                "        .iframe-class {\n" +
                "            border: 0;\n" +
                "            overflow: auto;\n" +
                "            height: 100vh;\n" +
                "            width: 100vw;\n" +
                "            position: absolute;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0px; background-color: black\">\n" +
                "    \n" +
                "    <script>\n" +
                "        var df = \"sdfiiousiog\"\n" +
                "    </script>\n" +
                "    <iframe src=\"" + streamAPIEndpoint + "\" allow=\"fullscreen\" style=\"position:fixed; top:0; left:0; bottom:0; right:0; width:100%; height:100%; border:none; margin:0; padding:0; overflow:hidden; z-index:999999;\">\n" +
                "        Your browser doesn't support iframes\n" +
                "    </iframe>\n" +
                "</body>\n" +
                "</html>";
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

    public static WebChromeClient createWebClient(AppCompatActivity activity){
        return new WebChromeClient() {
            private View mCustomView;
            private WebChromeClient.CustomViewCallback mCustomViewCallback;
            protected FrameLayout mFullscreenContainer;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;



            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                if (mCustomView != null) {
                    onHideCustomView();
                    return;
                }

                mCustomView = view;
                mOriginalSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
                mOriginalOrientation = activity.getRequestedOrientation();

                mCustomViewCallback = callback;
                mFullscreenContainer = activity.findViewById(R.id.fullScreenStreamer);
                mFullscreenContainer.addView(mCustomView, COVER_SCREEN_PARAMS);
                mFullscreenContainer.setVisibility(View.VISIBLE);
                activity.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null) {
                    return;
                }

                activity.getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
                activity.setRequestedOrientation(mOriginalOrientation);
                mCustomView.setVisibility(View.GONE);
                mFullscreenContainer.setVisibility(View.GONE);
                mFullscreenContainer.removeView(mCustomView);
                mCustomView = null;
                mFullscreenContainer = null;
                mCustomViewCallback.onCustomViewHidden();
            }
        };
    }


    public static WebViewClient createWebViewClient(){
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // This method will prevent any redirects
                return true; // true means the current WebView handles the URL
            }
        };
    }

}
