package com.hfad.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Rajeev on 03-02-2017.
 */

public class Playlist implements Parcelable {
    private String name;
    private ArrayList<Song> songList;
    public Playlist(String name, ArrayList<Song> songList){
        this.name=name;
        this.songList=songList;
    }
    public Playlist(Parcel in){
        name=in.readString();
        songList=in.readArrayList(Song.class.getClassLoader());

    }
    public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Playlist(Parcel) constructor
        out.writeString(name);
        out.writeList(songList);
        // Again continue doing this for the rest of your member data
    }
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public String getPlaylistName(){
        return name;
    }
    public ArrayList<Song> getPlaylist(){
        return songList;
    }
}
