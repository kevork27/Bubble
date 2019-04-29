package com.bubblestudios.bubble;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter2 extends RecyclerView.Adapter<CardViewHolder> implements Filterable {

    private List<DocumentSnapshot> snapshotList;
    private List<DocumentSnapshot> filteredSnapshotList;
    private StorageReference albumArtRef;
    private StorageReference snippetRef;
    private SimpleExoPlayer exoPlayer;
    private DataSource.Factory dataSourceFactory;
    private CardsFragment cardsFragment;
    private int firstLoad = 0;

    //constructor that sets each field to local variable
    public CardStackAdapter2(List<DocumentSnapshot> snapshotList, StorageReference albumArtRef, StorageReference snippetRef, SimpleExoPlayer exoPlayer, DataSource.Factory dataSourceFactory, CardsFragment cardsFragment) {
        this.snapshotList = snapshotList;
        this.albumArtRef = albumArtRef;
        this.snippetRef = snippetRef;
        this.exoPlayer = exoPlayer;
        this.dataSourceFactory = dataSourceFactory;
        this.cardsFragment = cardsFragment;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        return new CardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int i) {
        //get corresponding song for each card
        Snippet snippet = filteredSnapshotList.get(i).toObject(Snippet.class);

        //get values from snippet and set layout per card
        holder.artistName.setText(snippet.getArtist());
        holder.songTitle.setText(snippet.getTitle());
        holder.likedUsers.setText(snippet.getNumberOfLikes());
        Glide.with(holder.albumArt).load(albumArtRef.child(snippet.getAlbumArt())).into(holder.albumArt);
        holder.snippetRef = filteredSnapshotList.get(i).getReference();
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
        //on card click toggle music pause/play
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
            }
        });
    }

    @Override
    public int getItemCount() {
        //if the list of songs is not null return the size
        if(filteredSnapshotList == null) {
            return 0;
        } else {
            return filteredSnapshotList.size();
        }
    }

    public Snippet getItem(int pos) {
        //return the actual item given position
        return filteredSnapshotList.get(pos).toObject(Snippet.class);
    }

    //filter the list of songs by userID contained in liked_songs/disliked_songs so only previously unseen songs are shown
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userID = constraint.toString();
                if(userID.isEmpty()) {
                    filteredSnapshotList = snapshotList;
                } else {
                    List<DocumentSnapshot> tempSnapList = new ArrayList<>();
                    for(DocumentSnapshot snapshot: snapshotList) {
                        List<String> likedUsersList = snapshot.toObject(Snippet.class).getLiked_users();
                        List<String> dislikedUsersList = snapshot.toObject(Snippet.class).getDisliked_users();
                        if(!likedUsersList.contains(userID) && !dislikedUsersList.contains(userID)){
                            tempSnapList.add(snapshot);
                        }
                    }
                    filteredSnapshotList = tempSnapList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSnapshotList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSnapshotList = (List<DocumentSnapshot>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
