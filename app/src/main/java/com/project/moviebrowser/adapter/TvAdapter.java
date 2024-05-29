package com.project.moviebrowser.adapter;

import android.content.Context;
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
import com.project.moviebrowser.model.ModelTV;
import com.project.moviebrowser.networking.ApiEndpoint;

import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {

    private List<ModelTV> items;
    private TvAdapter.onSelectData onSelectData;
    private Context mContext;
    private double Rating;

    public interface onSelectData {
        void onSelected(ModelTV modelTV);
    }

    public TvAdapter(Context context, List<ModelTV> items, TvAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public TvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film, parent, false);
        return new TvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvAdapter.ViewHolder holder, int position) {
        final ModelTV data = items.get(position);

        holder.tvTitle.setText(data.getTitle());
        holder.tvReleaseDate.setText(data.getFirstAirDate());
        holder.tvDesc.setText(data.getOverview());

        if(data.getStreamLastEpisode()){
            holder.playButton.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_play_stream_round));
        } else if(data.getStreamFirstEpisode()){
            holder.playButton.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_play_trailer_round));
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
        public TextView tvTitle;
        public TextView tvReleaseDate;
        public TextView tvDesc;
        public RatingBar ratingBar;
        public ImageView playButton;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFilm = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.title);
            tvReleaseDate = itemView.findViewById(R.id.releaseDate);
            tvDesc = itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
