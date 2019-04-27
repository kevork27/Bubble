package com.bubblestudios.bubble;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Text;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public TextView artistName;
    public TextView songTitle;
    public ImageView albumArt;
    public MediaSource audioSource;
    public DocumentReference snippetRef;

    public CardViewHolder(final View itemView) {
        super(itemView);
        artistName = itemView.findViewById(R.id.artist_name);
        songTitle = itemView.findViewById(R.id.song_title);
        albumArt = itemView.findViewById(R.id.album_art_view);
    }
}
