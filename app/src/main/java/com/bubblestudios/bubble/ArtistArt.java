package com.bubblestudios.bubble;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class ArtistArt {

    private String artistArt;
    private DocumentReference artistArtRef;
    @ServerTimestamp
    private Date timeStamp;


public ArtistArt(String artistArt, DocumentReference artistArtRef) {
    this.artistArt = artistArt;
    this.artistArtRef = artistArtRef;
}
    public String getArtistArt() {
        return artistArt;
    }

    public void setArtistArt(String artistArt) {
        this.artistArt = artistArt;
    }

    public DocumentReference getArtistArtRef() {
        return artistArtRef;
    }

    public void setArtistArtRef(DocumentReference artistArtRef) {
        this.artistArtRef = artistArtRef;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }


}



