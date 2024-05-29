package com.project.moviebrowser.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.project.moviebrowser.R;
import com.project.moviebrowser.model.ModelMovie;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.Certification;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<ModelMovie> items;
    private MovieAdapter.onSelectData onSelectData;
    private Context mContext;
    private double Rating;

    public interface onSelectData {
        void onSelected(ModelMovie modelMovie);
    }

    public MovieAdapter(Context context, List<ModelMovie> items, MovieAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        final ModelMovie data = items.get(position);

        holder.movieTitle.setText(data.getTitle());
        holder.movieReleaseDate.setText(data.getReleaseDate());
        holder.movieDesc.setText(data.getOverview());

        if(data.isStreamAvailable()){
            holder.playButton.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_play_stream_round));
        } else if(data.isHasTrailer()){
            holder.playButton.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_play_trailer_round));
        }

        int drawableId = Certification.getDrawableId(data.getCertification());
        if(drawableId != -1){
            holder.ageRating.setForeground(ContextCompat.getDrawable(mContext, drawableId));
//            holder.ageRating.setBackground(ContextCompat.getDrawable(mContext, R.drawable.b));
        }

        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float) 0.5);
        holder.ratingBar.setRating(data.getRating());

        Glide.with(mContext)
                .load(data.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.cvFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvFilm;
        public ImageView imgPhoto;
        public TextView movieTitle;
        public TextView movieReleaseDate;
        public TextView movieDesc;
        public RatingBar ratingBar;
        public ImageView playButton, ageRating;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFilm = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            movieTitle = itemView.findViewById(R.id.title);
            movieReleaseDate = itemView.findViewById(R.id.releaseDate);
            movieDesc = itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            playButton = itemView.findViewById(R.id.playButton);
            ageRating = itemView.findViewById(R.id.ageRating);
        }
    }

}
