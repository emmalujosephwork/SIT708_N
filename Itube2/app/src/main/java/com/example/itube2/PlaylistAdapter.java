package com.example.itube2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<PlaylistItem> playlistItems;

    public PlaylistAdapter(List<PlaylistItem> playlistItems) {
        this.playlistItems = playlistItems;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem item = playlistItems.get(position);
        holder.videoUrlTextView.setText(item.getVideoUrl());
        holder.itemNumberTextView.setText(String.valueOf(position + 1)); // Number the list items

        holder.deleteButton.setOnClickListener(v -> {
            playlistItems.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView videoUrlTextView, itemNumberTextView;
        ImageView deleteButton;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            videoUrlTextView = itemView.findViewById(R.id.video_url_text);
            itemNumberTextView = itemView.findViewById(R.id.item_number);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
