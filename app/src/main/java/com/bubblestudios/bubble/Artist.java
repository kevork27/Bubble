package com.bubblestudios.bubble;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


//class artist contains the artist information
public class Artist {

    private String artistName;
    private String artistArt;
    private String artistBlurb; //description of artist
    @ServerTimestamp
    private Date timeStamp;

    //default constructor with no arguments
    public Artist() {
    }

    //constructor with arguments
    public Artist(String artistName, String artistBlurb, String artistArt) {
    this.artistName = artistName;
    this.artistBlurb = artistBlurb;
    this.artistArt = artistArt;
}

    //get and set functions for methods
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistArt() {
        return artistArt;
    }

    public void setArtistArt(String artistArt) {
        this.artistArt = artistArt;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getArtistBlurb() {
        return artistBlurb;
    }

    public void setArtistBlurb(String artistBlurb) {
        this.artistBlurb = artistBlurb;
    }

}



