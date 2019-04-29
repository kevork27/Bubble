package com.bubblestudios.bubble;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bubblestudios.bubble.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import io.grpc.Context;

public class ArtistProfileDialog extends DialogFragment {
//Establish necessary variables for Artist Profile Dialog
    public static String TAG = "SongDetailsDialog";
    TextView artistName;
    TextView artistBlurb;
    DocumentReference artistRef;
    StorageReference artistStorageRef;
    public ImageView artistArt;

//Assign dialog type (normal and fullscreen)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    //On create view, establish inflater reference as well as necessary bundles
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Unpackage bundle to reference snippet data for relevant data projection
        Bundle d_snips = getArguments();

        //View xml (artist_profile) via inflater, and establish elements within the xml to their R.id.values
        final View view = inflater.inflate(R.layout.artist_profile, container, false);
        artistBlurb = view.findViewById(R.id.description);
        artistName = view.findViewById(R.id.name);
        artistArt = view.findViewById(R.id.artistArt);

        //Reference Firebase in order to fetch the artist art image
        artistStorageRef = FirebaseStorage.getInstance().getReference().child("ArtistArt");

        //Retrieve the artist name from bundle
        artistName.setText(d_snips.getString("artistName"));

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


                        //Use Glide to load artistArt image into the artistArt reference of xml artist_profile.xml
                        Glide.with(view.findViewById(R.id.artistArt)).load(artistStorageRef.child(artist.getArtistArt())).placeholder(R.drawable.icon).into(artistArt);


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