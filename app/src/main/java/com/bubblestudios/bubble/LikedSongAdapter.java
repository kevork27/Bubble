package com.bubblestudios.bubble;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class LikedSongAdapter extends RecyclerView.Adapter<LikedSongHolder> implements Filterable {

    private List<DocumentSnapshot> snapshotList;
    private List<DocumentSnapshot> filteredSnapshotList;
    private StorageReference albumArtRef;
    private Snippet snippet;
    private Context context;

    //constructor to set local variables from parameters
    public LikedSongAdapter(List<DocumentSnapshot> snapshotList, StorageReference albumArtRef, Context context) {
        this.snapshotList = snapshotList;
        this.albumArtRef = albumArtRef;
        this.context = context;
    }

    @NonNull
    @Override
    public LikedSongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        return new LikedSongHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.liked_song_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikedSongHolder holder, int i) {
        //get specific snippet per position
        snippet = filteredSnapshotList.get(i).toObject(Snippet.class);

        //get and set data for UI per liked song
        holder.songTitle.setText(snippet.getTitle());
        holder.artistName.setText(snippet.getArtist());
        Glide.with(holder.albumArt).load(albumArtRef.child(snippet.getAlbumArt())).into(holder.albumArt);

        //Create Bundles so that snippet data can be translated to dialog
        final Bundle snips = new Bundle();
        snips.putString("songTitle",snippet.getTitle());
        snips.putString("artistName",snippet.getArtist());
        snips.putString("songBlurb",snippet.getSongBlurb());

        final Bundle aSnips = new Bundle();
        aSnips.putString("songTitle",snippet.getTitle());
        aSnips.putString("artistName",snippet.getArtist());

        //Gets the artist reference from the snippet
        DocumentReference artistRef = snippet.getArtistRef();
        //if not null, put the artistref path in the bundle
        if(artistRef != null) {
            //put the path (in firestore) of the reference in the bundle
            aSnips.putString("artistRefPath", artistRef.getPath());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Listens for click on each entry in recyclerView entries
                //Opens new SongDetailsDialog for the clicked entry
                SongDetailsDialog dialog = new SongDetailsDialog();
                FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                dialog.setArguments(snips);
                dialog.show(ft, SongDetailsDialog.TAG);

                

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //Listens for long click on each entry in recyclerView entries
                //Opens new ArtistDetailsDialog for the clicked entry

                ArtistProfileDialog a_dialog = new ArtistProfileDialog();
                FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                a_dialog.setArguments(aSnips);
                a_dialog.show(ft, ArtistProfileDialog.TAG);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        //if not null return size of list
        if(filteredSnapshotList == null) {
            return 0;
        } else {
            return filteredSnapshotList.size();
        }
    }

    public Snippet getItem(int pos) {
        //return specific item for position
        return filteredSnapshotList.get(pos).toObject(Snippet.class);
    }

    public DocumentReference getItemReference(int pos) {
        //return firebase reference of specific item for position
        return filteredSnapshotList.get(pos).getReference();
    }

    public void removeItem(int pos) {
        //delete item from list given position
        //using this instead of passing new data because of filter below
        filteredSnapshotList.remove(pos);
        notifyDataSetChanged();
    }

    //search filter, given the query string from the userprofilefragment, will check for matches in song title or artist name
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString().toLowerCase();
                if(searchString.isEmpty()) {
                    filteredSnapshotList = snapshotList;
                } else {
                    List<DocumentSnapshot> tempSnapList = new ArrayList<>();
                    for(DocumentSnapshot snapshot: snapshotList) {
                        Snippet snippet = snapshot.toObject(Snippet.class);
                        String songTitle = snippet.getTitle();
                        String artistName = snippet.getArtist();
                        if (songTitle.toLowerCase().contains(searchString) || artistName.toLowerCase().contains(searchString)) {
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
