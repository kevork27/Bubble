package com.bubblestudios.bubble;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Snippet {
    private String title;
    private String artist;
    private String snippet;
    private String albumArt;
    @ServerTimestamp private Date timeStamp;

    public Snippet(String title, String artist, String snippet, String albumArt) {
        this.title = title;
        this.artist = artist;
        this.snippet = snippet;
        this.albumArt = albumArt;
    }

    public Snippet() {}

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

}
