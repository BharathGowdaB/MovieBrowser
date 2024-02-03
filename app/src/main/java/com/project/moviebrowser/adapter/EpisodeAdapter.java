package com.project.moviebrowser.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public EpisodeAdapter(List<TVShowEpisode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TVShowEpisode episode = episodes.get(position);
        holder.name.setText(episode.getName());
        holder.number.setText(String.valueOf(episode.getNumber()));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StreamService.hasStreamableService(episode.getStreamUri().toString())){
                    Intent intent = new Intent(Intent.ACTION_VIEW, episode.getStreamUri());

                    String title = "Select a browser";
                    Intent chooser = Intent.createChooser(intent, title);

                    // Verify the original intent will resolve to at least one activity
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        getApplicationContext().startActivity(chooser);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Stream Not Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.episode_name);
            number = itemView.findViewById(R.id.episode_number);
        }
    }
}
