package com.hfad.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rajeev on 09-01-2017.
 */

public class Song implements Parcelable {
    private long songId;
    private String artist;
    private String title;
    public Song(long id, String title, String artist){
        this.artist=artist;
        this.title=title;
        this.songId=id;
    }
    private Song(Parcel in){
        songId=in.readLong();
        artist=in.readString();
        title=in.readString();
    }
    public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Song(Parcel) constructor
        out.writeLong(songId);
        out.writeString(artist);
        out.writeString(title);
        // Again continue doing this for the rest of your member data
    }
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    public long getId(){
        return songId;
    }
    public String getArtist(){
        return artist;
    }
    public String getTitle(){
        return title;
    }

}
