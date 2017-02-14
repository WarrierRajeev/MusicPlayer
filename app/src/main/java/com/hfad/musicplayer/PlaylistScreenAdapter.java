package com.hfad.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rajeev on 03-02-2017.
 */

public class PlaylistScreenAdapter extends BaseAdapter {
    private ArrayList<Playlist> playlist;                                                                  //as a view in relation to the
    private LayoutInflater playlistInf;
    Context cont;



    //entire list. The adapter class
    public PlaylistScreenAdapter(Context c, ArrayList<Playlist> thePlaylist){                                        //does this automatically
        playlist=thePlaylist;
        playlistInf=LayoutInflater.from(c);
        this.cont=c;
    }
    @Override
    public int getCount() {
        return playlist.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //map to playlist layout
        LinearLayout playlistLay = (LinearLayout)playlistInf.inflate(R.layout.playlist_name, parent, false);
        //get title and artist views
        TextView playlistView = (TextView)playlistLay.findViewById(R.id.playlist_title);
        //get song using position
        Playlist currPlaylist = playlist.get(position);
        //get title and artist strings
        playlistView.setText(currPlaylist.getPlaylistName());
        //set position as tag
        playlistLay.setTag(position);                                                           //This tag is used to start onclick

        return playlistLay;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
}
