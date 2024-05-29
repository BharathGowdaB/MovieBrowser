package com.project.moviebrowser.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.project.moviebrowser.activities.DetailTelevisionActivity;
import com.project.moviebrowser.adapter.TvAdapter;
import com.project.moviebrowser.configuration.Configuration;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.model.ModelTV;
import com.project.moviebrowser.model.TVShowSeason;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.Certification;
import com.project.moviebrowser.networking.VidSrc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FragmentTV extends Fragment implements  TvAdapter.onSelectData{

    private RecyclerView  rvFilmRecommend;
    private TvAdapter tvAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchFilm;
    private List<ModelTV> tvList = new ArrayList<>();

    private TextView titleView;
    private RadioGroup filterOption;
    private Button filterButton, loadMoreButton;
    private String searchQuery = "";
    public FragmentTV() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        titleView = rootView.findViewById(R.id.listTitle);
        titleView.setText("Popular Shows");
        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim().compareTo(searchQuery) != 0){
                    searchQuery = query.trim();
                    Configuration.getInstance().resetNextPage();
                    tvList = new ArrayList<>();
                    getTvShow();
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
                tvList = new ArrayList<>();
                getTvShow();
            }
        });

        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        loadMoreButton = rootView.findViewById(R.id.loadMore);

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTvShow();
            }
        });

        Configuration.getInstance().resetNextPage();
        getTvShow();

        return rootView;
    }

    private void getTvShow() {
        progressDialog.show();
        String url = ApiEndpoint.MOVIEBROWSER_API + "tvshow/";

        if (searchQuery.length() != 0) {
            url += "search";
        } else {
            switch (filterOption.getCheckedRadioButtonId()){
                case R.id.filter_latest:        url += "newly-added";
                                                titleView.setText("Newly Added Shows");
                                                break;
                case R.id.filter_topRated:      url += "top-rated";
                                                titleView.setText("Top Rated Showa");
                                                break;
                case R.id.filter_upcoming:      url += "upcoming";
                                                titleView.setText("Upcoming Shows");
                                                break;
                case R.id.filter_popularity:    ;
                default:                        url += "popular";
                                                titleView.setText("Popular Shows");
            }
        }

        AndroidNetworking.get(url)
                .addQueryParameter("page", String.valueOf(Configuration.getInstance().getNextPage()))
                .addQueryParameter("offset", String.valueOf(Configuration.getInstance().getNextOffset()))
                .addQueryParameter("count", "12")
                .addQueryParameter("certification", Certification.getCertification(Configuration.getInstance().getCertificationLevel()))
                .addQueryParameter("query", searchQuery)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            int startPosition = tvList.size();
                            int count = jsonArray.length();
                            if(count == 0){
                                loadMoreButton.setVisibility(View.GONE);
                            } else {
                                loadMoreButton.setVisibility(View.VISIBLE);
                                for (int i = 0; i < count; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ModelTV dataApi = ModelTV.createModel(jsonObject);
                                    tvList.add(dataApi);
                                }
                            }
                            showTvShow(startPosition, count);
                            Configuration.getInstance().setNextPage(response.getInt("nextPage"));
                            Configuration.getInstance().setNextOffset(response.getInt("nextOffset"));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to display data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showTvShow(int startPosition, int count) {
        tvAdapter = new TvAdapter(getActivity(), tvList, this);
        rvFilmRecommend.setAdapter(tvAdapter);
        tvAdapter.notifyItemRangeInserted(startPosition, count);
    }

    @Override
    public void onSelected(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), DetailTelevisionActivity.class);
        intent.putExtra("detailTV", modelTV);
        startActivity(intent);
    }
}
