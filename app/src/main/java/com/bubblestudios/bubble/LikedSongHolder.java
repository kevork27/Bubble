package com.bubblestudios.bubble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.support.v4.content.ContextCompat.startActivity;

public class LikedSongHolder extends RecyclerView.ViewHolder {


    public TextView artistName;
    public TextView songTitle;
    public ImageView albumArt;


    public LikedSongHolder(@NonNull View itemView) {
        super(itemView);
        artistName = itemView.findViewById(R.id.liked_artist_name);
        songTitle = itemView.findViewById(R.id.liked_song_title);
        albumArt = itemView.findViewById(R.id.liked_album_art);



    }


}

