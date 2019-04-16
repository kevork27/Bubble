package com.bubblestudios.bubble;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

public class CardsFragment extends Fragment implements CardStackListener {

    private OnFragmentInteractionListener mListener;
    private CardStackAdapter adapter;
    private SimpleExoPlayer exoPlayer;
    private DataSource.Factory dataSourceFactory;
    private PlayerView playerView;
    private CardStackView cardStackView;
    private DocumentReference snippetRef;
    private Snippet currentSnippet;
    private String userID;
    private FirebaseFirestore db;

    public CardsFragment() {
    }

    public static CardsFragment newInstance() {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cards, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference albumArtRef = storageRef.child("AlbumArt");
        final StorageReference snippetRef = storageRef.child("Snippets");

        playerView = view.findViewById(R.id.player_view);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "Bubble"));
        playerView.setPlayer(exoPlayer);

        Query query = FirebaseFirestore.getInstance().collection("snippets").orderBy("timeStamp");
        FirestoreRecyclerOptions<Snippet> options = new FirestoreRecyclerOptions.Builder<Snippet>().setQuery(query, Snippet.class).build();

        adapter = new CardStackAdapter(options, albumArtRef, snippetRef, exoPlayer, dataSourceFactory, this);
        cardStackView = view.findViewById(R.id.card_view);
        cardStackView.setAdapter(adapter);
        CardStackLayoutManager layoutManager = new CardStackLayoutManager(getContext(), this);
        cardStackView.setLayoutManager(layoutManager);

        final Button pausePlayButton = view.findViewById(R.id.pause_play_button);
        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void firstPlay() {
        CardViewHolder viewHolder = (CardViewHolder) cardStackView.findViewHolderForAdapterPosition(0);
        exoPlayer.prepare(viewHolder.audioSource);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("direction", "onCardSwiped: " + direction.toString() + " ref: " + snippetRef.getId());
        if(direction == Direction.Right) {
            db.collection("users").document(userID).collection("liked").add(currentSnippet);
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        CardViewHolder viewHolder = (CardViewHolder) cardStackView.findViewHolderForAdapterPosition(position);
        exoPlayer.prepare(viewHolder.audioSource);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        exoPlayer.setPlayWhenReady(false);
        Log.d("disappear", "onCardDisappeared: " + position);
        CardViewHolder viewHolder = (CardViewHolder) cardStackView.findViewHolderForAdapterPosition(position);
        snippetRef = viewHolder.snippetRef;
        currentSnippet = adapter.getItem(position);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}