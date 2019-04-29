package com.bubblestudios.bubble;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bubblestudios.bubble.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SongDetailsDialog extends DialogFragment {

    //Establish the necessary variables for SongDetails Dialog
    public static String TAG = "SongDetailsDialog";
    //public TextView songBlurb;
    TextView artistName;
    TextView songTitle;
    TextView songBlurb;
    ImageView albumArt;
    Snippet snippet;
    //TextView songBlurb;

    //Initialize the type of Dialog (full screen)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    //Initialize bundles and inflater types for dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        //Unpackage bundle with snippet data
        Bundle d_snips = getArguments();

        //Display inflater (song details Dialog) and set variables to layout objects
        View view = inflater.inflate(R.layout.song_details_dialog, container, false);
        songTitle = view.findViewById(R.id.details_song_title);
        songBlurb = view.findViewById(R.id.details_song_blurb);
        artistName = view.findViewById(R.id.details_artist_name);

        // Use d_snips Bundle to access snippet info
        //Assign attributes via unpackaged bundle
        songBlurb.setText(d_snips.getString("songBlurb"));
        songTitle.setText(d_snips.getString("songTitle"));
        artistName.setText(d_snips.getString("artistName"));
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