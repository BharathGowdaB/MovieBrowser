package com.project.moviebrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.project.moviebrowser.R;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.model.ModelTV;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.Certification;

import java.util.List;

import io.realm.RealmObject;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    private List<RealmObject> items;
    private Context mContext;
    private RecommendationAdapter.openRecommendation openRecommended;
    private boolean isMovieList;

    public interface openRecommendation {
        void openRecommended(RealmObject model);
    }

    public RecommendationAdapter(Context context, List<RealmObject> list, RecommendationAdapter.openRecommendation xOpenRecommended, Boolean isMovieList){
        this.mContext = context;
        this.items = list;
        this.openRecommended = xOpenRecommended;
        this.isMovieList = isMovieList;
    }
    @NonNull
    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster, parent, false);
        return new RecommendationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.ViewHolder holder, int position) {
        int playButtonDrawableId = -1, ageRatingDrawableId = -1;
        String posterPath;
        RealmObject data = items.get(position);
        if(isMovieList) {
            ModelMovie model = (ModelMovie) data;
            if(model.isStreamAvailable()){
                playButtonDrawableId = R.mipmap.ic_play_stream_round;
            } else if(model.isHasTrailer()){
                playButtonDrawableId = R.mipmap.ic_play_trailer_round;
            }

            ageRatingDrawableId = Certification.getDrawableId(model.getCertification());
            posterPath = model.getPosterPath();
        } else {
            ModelTV model = (ModelTV) data;
            if(model.getStreamLastEpisode()){
                playButtonDrawableId = R.mipmap.ic_play_stream_round;
            } else if(model.getStreamFirstEpisode()){
                playButtonDrawableId = R.mipmap.ic_play_trailer_round;
            }

            ageRatingDrawableId = Certification.getDrawableId(model.getCertification());
            posterPath = model.getPosterPath();
        }

        if(playButtonDrawableId != -1) {
            holder.playButton.setBackground(ContextCompat.getDrawable(mContext, playButtonDrawableId));
        }
        if(ageRatingDrawableId != -1){
            holder.ageRating.setForeground(ContextCompat.getDrawable(mContext, ageRatingDrawableId));
//            holder.ageRating.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_rounded));
        }

        Glide.with(mContext)
                .load(posterPath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecommended.openRecommended(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPhoto, playButton, ageRating;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            playButton = itemView.findViewById(R.id.playButton);
            ageRating = itemView.findViewById(R.id.ageRating);
        }
    }
}
