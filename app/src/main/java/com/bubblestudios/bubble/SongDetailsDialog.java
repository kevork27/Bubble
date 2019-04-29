package com.bubblestudios.bubble;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SongDetailsDialog extends DialogFragment {

    public static String TAG = "SongDetailsDialog";
    //public TextView songBlurb;
    TextView artistName;
    TextView songTitle;
    ImageView albumArt;
    Snippet snippet;
    //TextView songBlurb;

    public void attachSnippet(Snippet snippet) {

        this.snippet = snippet;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Bundle d_snips = getArguments();


        View view = inflater.inflate(R.layout.song_details_dialog, container, false);
        artistName = view.findViewById(R.id.details_artist_name);
        songTitle = view.findViewById(R.id.details_song_title);
        albumArt = view.findViewById(R.id.details_album_art);

        // Use d_snips Bundle to access snippet info
        //songTitle.setText(d_snips.getString("songTitle"));
        artistName.setText(d_snips.getString("artistName"));
        songTitle.setText(d_snips.getString("songBlurb"));
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