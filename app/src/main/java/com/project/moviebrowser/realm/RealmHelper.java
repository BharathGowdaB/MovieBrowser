package com.project.moviebrowser.realm;

import android.content.Context;

import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.model.ModelTV;
import com.project.moviebrowser.model.TVShowSeason;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmHelper {

    private Context mContext;
    private Realm realm;
    private RealmResults<ModelMovie> modelMovie;
    private RealmResults<ModelTV> modelTV;

    public RealmHelper(Context mContext) {
        this.mContext = mContext;
        Realm.init(mContext);
        realm = Realm.getDefaultInstance();
    }

    public ArrayList<ModelMovie> showFavoriteMovie() {
        ArrayList<ModelMovie> data = new ArrayList<>();
        modelMovie = realm.where(ModelMovie.class).findAll();

        if (modelMovie.size() > 0) {
            for (int i = 0; i < modelMovie.size(); i++) {
                ModelMovie movie = new ModelMovie();
                movie.setId(modelMovie.get(i).getId());
                movie.setTitle(modelMovie.get(i).getTitle());
                movie.setVoteAverage(modelMovie.get(i).getVoteAverage());
                movie.setOverview(modelMovie.get(i).getOverview());
                movie.setReleaseDate(modelMovie.get(i).getReleaseDate());
                movie.setPosterPath(modelMovie.get(i).getPosterPath());
                movie.setBackdropPath(modelMovie.get(i).getBackdropPath());
                movie.setPopularity(modelMovie.get(i).getPopularity());
                movie.setRating(modelMovie.get(i).getRating());
                data.add(movie);
            }
        }
        return data;
    }

    public ArrayList<ModelTV> showFavoriteTV() {
        ArrayList<ModelTV> data = new ArrayList<>();
        modelTV = realm.where(ModelTV.class).findAll();

        if (modelTV.size() > 0) {
            for (int i = 0; i < modelTV.size(); i++) {
                ModelTV tv = new ModelTV();
                tv.setId(modelTV.get(i).getId());
                tv.setTitle(modelTV.get(i).getTitle());
                tv.setVoteAverage(modelTV.get(i).getVoteAverage());
                tv.setOverview(modelTV.get(i).getOverview());
                tv.setFirstAirDate(modelTV.get(i).getFirstAirDate());
                tv.setPosterPath(modelTV.get(i).getPosterPath());
                tv.setBackdropPath(modelTV.get(i).getBackdropPath());
                tv.setPopularity(modelTV.get(i).getPopularity());
                data.add(tv);
            }
        }
        return data;
    }

    public void addFavoriteMovie(ModelMovie movie) {
        if(isFavoriteMovie(movie.getId())){
            return;
        }

        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();
    }
    public void addFavoriteMovie(int Id, String Title, double VoteAverage, String Overview,
                            String ReleaseDate, String PosterPath, String BackdropPath, String Popularity, float Rating) {
        if(isFavoriteMovie(Id)){
            return;
        }
        ModelMovie movie = new ModelMovie();
        movie.setId(Id);
        movie.setTitle(Title);
        movie.setVoteAverage(VoteAverage);
        movie.setOverview(Overview);
        movie.setReleaseDate(ReleaseDate);
        movie.setPosterPath(PosterPath);
        movie.setBackdropPath(BackdropPath);
        movie.setPopularity(Popularity);
        movie.setRating(Rating);

        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();

    }

    public void addFavoriteTV(ModelTV tvshow) {
        if(isFavoriteTV(tvshow.getId())){
            return;
        }

        realm.beginTransaction();
        realm.copyToRealm(tvshow);
        realm.commitTransaction();
    }

    public void addFavoriteTV(int Id, String Title, double VoteAverage, String Overview,
                              String ReleaseDate, String PosterPath, String BackdropPath, String Popularity, TVShowSeason[] seasons) {
        if(isFavoriteTV(Id)){
            return;
        }
        ModelTV tv = new ModelTV();
        tv.setId(Id);
        tv.setTitle(Title);
        tv.setVoteAverage(VoteAverage);
        tv.setOverview(Overview);
        tv.setFirstAirDate(ReleaseDate);
        tv.setPosterPath(PosterPath);
        tv.setBackdropPath(BackdropPath);
        tv.setPopularity(Popularity);
        //tv.setSeasons(seasons);

        realm.beginTransaction();
        realm.copyToRealm(tv);
        realm.commitTransaction();

    }

    public boolean isFavoriteMovie(int id){
        realm.beginTransaction();
        RealmResults<ModelMovie> modelMovie = realm.where(ModelMovie.class).findAll();
        realm.commitTransaction();
        for (ModelMovie mv: modelMovie) {
            if (mv.getId() == id) {
                return true;
            }
        }
        return  false;
    }

    public boolean isFavoriteTV(int id){
        realm.beginTransaction();
        RealmResults<ModelTV> modelTV = realm.where(ModelTV.class).findAll();
        realm.commitTransaction();
        for (ModelTV mv: modelTV) {
            if (mv.getId() == id) {
                return true;
            }
        }
        return  false;
    }

    public void deleteFavoriteMovie(int id) {
        realm.beginTransaction();
        RealmResults<ModelMovie> modelMovie = realm.where(ModelMovie.class).equalTo("Id",id).findAll();

        modelMovie.deleteAllFromRealm();

        realm.commitTransaction();

    }

    public void deleteFavoriteTV(int id) {
        realm.beginTransaction();
        RealmResults<ModelTV> modelTV = realm.where(ModelTV.class).equalTo("Id",id).findAll();
        modelTV.deleteAllFromRealm();
        realm.commitTransaction();
    }

}
