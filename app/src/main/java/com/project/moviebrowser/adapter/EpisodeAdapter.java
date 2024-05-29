package com.project.moviebrowser.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.project.moviebrowser.R;
import com.project.moviebrowser.activities.DetailTelevisionActivity;
import com.project.moviebrowser.model.TVShowEpisode;
import com.project.moviebrowser.services.StreamService;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    private List<TVShowEpisode> episodes;
    private WebView webView;

    private int selectedPosition;
    public EpisodeAdapter(List<TVShowEpisode> episodes, WebView webView) {
        this.episodes = episodes;
        this.webView = webView;
        selectedPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TVShowEpisode episode = episodes.get(position);
        holder.number.setText(String.valueOf(episode.getNumber()));
        holder.itemView.setSelected(position == selectedPosition);
        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StreamService streamer = new StreamService(episode.getShowId(), episode.getSeasonNumber(), episode.getNumber());
                webView.loadData(streamer.generateStreamHTML(), "text/html", "UTF-8");
                notifyItemChanged(selectedPosition);  // refresh previous selected item
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                /*
                if(StreamService.hasStreamableService(episode.getStreamer().getStreamUri().toString())){
                    Intent intent = new Intent(Intent.ACTION_VIEW, episode.getStreamer().getStreamUri());

                    String title = "Select a browser";
                    Intent chooser = Intent.createChooser(intent, title);

                    // Verify the original intent will resolve to at least one activity
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        getApplicationContext().startActivity(chooser);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Stream Not Found", Toast.LENGTH_SHORT).show();
                }
                */

            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.episode_number);
        }
    }
}
