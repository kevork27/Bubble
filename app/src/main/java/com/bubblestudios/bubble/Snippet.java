package com.bubblestudios.bubble;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

//class snippet is defined for each song clip, it contains the main characteristics of a song as methods
public class Snippet {
    private String title;
    private String artist;
    private String snippet;
    private String albumArt; //picture of album ocover
    private String songBlurb; // description of the song
    private DocumentReference artistRef; // reference to the artists in Firebase
    private List<String> liked_users;
    private List<String> disliked_users;
    @ServerTimestamp private Date timeStamp;

    //constructor with arguments
    public Snippet(String title, String artist, String songBlurb, String snippet, String albumArt, List<String> liked_users, List<String> disliked_users, DocumentReference artistRef) {
        this.title = title;
        this.artist = artist;
        this.songBlurb = songBlurb;
        this.snippet = snippet;
        this.albumArt = albumArt;
        this.liked_users = liked_users;
        this.disliked_users = disliked_users;
        this.artistRef = artistRef;
        }

    public Snippet() {} // constructor with no arguments

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongBlurb(){
        return songBlurb;
    }

    public void setSongBlurb(String songBlurb){
        this.songBlurb = songBlurb;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getLiked_users() {
        return liked_users;
    }

    public void setLiked_users(List<String> liked_users) {
        this.liked_users = liked_users;
    }

    public List<String> getDisliked_users() {
        return disliked_users;
    }

    public void setDisliked_users(List<String> disliked_users) {
        this.disliked_users = disliked_users;
    }

    public DocumentReference getArtistRef() {
        return artistRef;
    }

    public void setArtistRef(DocumentReference artistRef) {
        this.artistRef = artistRef;
    }

}
