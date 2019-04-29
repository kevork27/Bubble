package com.bubblestudios.bubble;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ArtistProfileDialog extends DialogFragment {

    public static String TAG = "SongDetailsDialog";
    TextView artistName;
    TextView artistBlurb;
    DocumentReference artistRef;
    StorageReference artistStorageRef;
    public ImageView artistArt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // Artist artist = snapshotList.get(i).toObject(Artist.class);
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle d_snips = getArguments();
        final View view = inflater.inflate(R.layout.artist_profile, container, false);
        artistBlurb = view.findViewById(R.id.description);
        artistName = view.findViewById(R.id.name);
        artistArt = view.findViewById(R.id.artistArt);

        artistStorageRef = FirebaseStorage.getInstance().getReference().child("ArtistArt");

        artistName.setText(d_snips.getString("artistName"));
        //artistBlurb.setText(d_snips.getString("artistBlurb")); // moved this to the artist object (get it below from artistRef)

        //retrieve the reference path from the bundle
        String artistRefPath = d_snips.getString("artistRefPath");


        //check if it exists
        if(artistRefPath!=null){
            //create a document reference from the path
            //you can get the artist object from here now and then the artistArt from that
            artistRef = FirebaseFirestore.getInstance().document(artistRefPath);
            artistRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        final Artist artist = task.getResult().toObject(Artist.class);
                        //now you can do artist.getWhatever and pass it to glide, or get the blurb, etc.

                        //Set artist blurb text
                        artistBlurb.setText(artist.getArtistBlurb());

                        //Put image into holder via glide

                        //The commented Glide below is the correct glide syntax, but I cannot figure out how to get artist Storage Ref into this scope.
                        //I know that I should establish artistStorageRef in Liked song adapter, as you have in CardStackAdapter2, but I am not sure how to
                        //declare that in LikedSongAdapter and then bundle into ArtistProfileDialog
                        Glide.with(view.findViewById(R.id.artistArt)).load(artistStorageRef.child(artist.getArtistArt())).placeholder(R.drawable.icon).into(artistArt);


                        //Temporary Glide with placeholder
                        //Glide.with(view.findViewById(R.id.artistArt)).load(artist.getArtistArt()).placeholder(R.drawable.icon).into(artistArt);

                    }
                }

            });

        }

        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }





}