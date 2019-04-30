package com.bubblestudios.bubble;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class CardStackAdapter extends FirestoreRecyclerAdapter<Snippet, CardViewHolder> {

    private StorageReference albumArtRef;
    private StorageReference snippetRef;
    private SimpleExoPlayer exoPlayer;
    private DataSource.Factory dataSourceFactory;
    private CardsFragment cardsFragment;
    private int firstLoad = 0;


    public CardStackAdapter(@NonNull FirestoreRecyclerOptions options, StorageReference albumArtRef, StorageReference snippetRef, SimpleExoPlayer exoPlayer, DataSource.Factory dataSourceFactory, CardsFragment cardsFragment) {
        super(options);
        this.albumArtRef = albumArtRef;
        this.snippetRef = snippetRef;
        this.exoPlayer = exoPlayer;
        this.dataSourceFactory = dataSourceFactory;
        this.cardsFragment = cardsFragment;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CardViewHolder holder, int position, @NonNull final Snippet snippet) {
        holder.artistName.setText(snippet.getArtist());
        holder.songTitle.setText(snippet.getTitle());
        Glide.with(holder.albumArt).load(albumArtRef.child(snippet.getAlbumArt())).into(holder.albumArt);
        holder.snippetRef = getSnapshots().getSnapshot(position).getReference();

        snippetRef.child(snippet.getSnippet()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                MediaSource audioSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                holder.audioSource = audioSource;

                if((firstLoad == 0) && (holder.getAdapterPosition() == 0)) {
                    cardsFragment.firstPlay();
                    firstLoad = 1;
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
            }
        });

    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false));
    }


}
