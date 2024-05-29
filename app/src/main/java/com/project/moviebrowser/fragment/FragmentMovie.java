package com.project.moviebrowser.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.project.moviebrowser.R;
import com.project.moviebrowser.activities.DetailMovieActivity;
import com.project.moviebrowser.adapter.MovieAdapter;
import com.project.moviebrowser.adapter.RecommendationAdapter;
import com.project.moviebrowser.configuration.Configuration;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.Certification;
import com.project.moviebrowser.networking.VidSrc;
import com.project.moviebrowser.view.RadioSelector;
import com.project.moviebrowser.view.TriSelector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class FragmentMovie extends Fragment implements  MovieAdapter.onSelectData {

    private RecyclerView  rvFilmRecommend;
    private MovieAdapter movieAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchFilm;
    private List<ModelMovie> movieList = new ArrayList<>();
    private List<ModelMovie> recdMovies = new ArrayList<>();
    private TextView titleView;
    RadioGroup filterOption;
    Button filterButton, loadMoreButton;

    private String searchQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        titleView = rootView.findViewById(R.id.listTitle);
        titleView.setText("Popular Movies");
        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim().compareTo(searchQuery) != 0){
                    searchQuery = query.trim();
                    Configuration.getInstance().resetNextPage();
                    movieList = new ArrayList<>();
                    getMovie();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("") && newText.length() != searchQuery.length()){
                    searchQuery = "";
                }
                return false;
            }
        });

        filterOption = rootView.findViewById(R.id.filterOptions);
        filterButton = rootView.findViewById(R.id.filter_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.findViewById(R.id.filterOptions).setVisibility(View.GONE);
                Configuration.getInstance().resetNextPage();
                movieList = new ArrayList<>();
                getMovie();
            }
        });
        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        loadMoreButton = rootView.findViewById(R.id.loadMore);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMovie();
            }
        });

        Configuration.getInstance().resetNextPage();
        getMovie();

        return rootView;
    }

    @SuppressLint("NonConstantResourceId")
    private void getMovie() {
        progressDialog.show();
        String url = ApiEndpoint.MOVIEBROWSER_API + "movie/";

        if (searchQuery.length() != 0) {
            url += "search";
            titleView.setText("Search Result");
        } else {
            switch (filterOption.getCheckedRadioButtonId()){
                case R.id.filter_latest:        url += "newly-added";
                                                titleView.setText("Newly Added Movies");
                                                break;
                case R.id.filter_topRated:      url += "top-rated";
                                                titleView.setText("Top Rated Movies");
                                                break;
                case R.id.filter_upcoming:      url += "upcoming";
                                                titleView.setText("Upcoming Movies");
                                                break;
                case R.id.filter_popularity:    ;
                default:                        url += "popular";
                                                titleView.setText("Popular Movies");
            }
        }

        AndroidNetworking.get(url)
                .addQueryParameter("page", String.valueOf(Configuration.getInstance().getNextPage()))
                .addQueryParameter("offset", String.valueOf(Configuration.getInstance().getNextOffset()))
                .addQueryParameter("count", "10")
                .addQueryParameter("certification", Certification.getCertification(Configuration.getInstance().getCertificationLevel()))
                .addQueryParameter("query", searchQuery)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            int startPosition = movieList.size();
                            int count = jsonArray.length();
                            if(count == 0) {
                                loadMoreButton.setVisibility(View.GONE);
                            } else {
                                loadMoreButton.setVisibility(View.VISIBLE);
                                for (int i = 0; i < count; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ModelMovie dataApi = ModelMovie.createModel(jsonObject);
                                    movieList.add(dataApi);
                                }
                            }
                            showMovie(startPosition, count);
                            Configuration.getInstance().setNextPage(response.getInt("nextPage"));
                            Configuration.getInstance().setNextOffset(response.getInt("nextOffset"));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMovie(int startPosition, int count) {
        movieAdapter = new MovieAdapter(getActivity(), movieList, this);
        rvFilmRecommend.setAdapter(movieAdapter);
        movieAdapter.notifyItemRangeInserted(startPosition, count);
    }

    @Override
    public void onSelected(ModelMovie modelMovie) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("detailMovie", modelMovie);
        startActivity(intent);
    }

}