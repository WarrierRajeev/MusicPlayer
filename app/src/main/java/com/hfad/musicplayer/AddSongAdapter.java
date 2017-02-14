package com.hfad.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class AddSongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;                                                                  //as a view in relation to the
    private LayoutInflater songInf;
    Context cont;
    final ArrayList<Integer> coloredItems=new ArrayList<Integer>();


    //entire list. The adapter class
    public AddSongAdapter(Context c, ArrayList<Song> theSongs){                                        //does this automatically
        songs=theSongs;
        songInf=LayoutInflater.from(c);
        this.cont=c;

    }
    @Override
    public int getCount() {
        return songs.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final int pos = position;


        if(view == null){

            final View viewClick = view;

            LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.song, null);

        }
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);                                                           //This tag is used to start service

        songView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED POSITION",Integer.valueOf(pos).toString());
                View parent = (View) v.getParent();




                if(!coloredItems.contains(Integer.valueOf(pos))) {
                    parent.setBackgroundColor(Color.GREEN);
                    coloredItems.add(Integer.valueOf(pos));

                }else{
                    parent.setBackgroundColor(Color.TRANSPARENT);
                    coloredItems.remove(Integer.valueOf(pos));
                }
            }
        });
        artistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED POSITION",Integer.valueOf(pos).toString());
                View parent = (View) v.getParent();




                if(!coloredItems.contains(Integer.valueOf(pos))) {
                    parent.setBackgroundColor(Color.GREEN);
                    coloredItems.add(Integer.valueOf(pos));

                }else{
                    parent.setBackgroundColor(Color.TRANSPARENT);
                    coloredItems.remove(Integer.valueOf(pos));
                }
            }
        });
        if(coloredItems.contains(Integer.valueOf(position)))
            view.setBackgroundColor(Color.GREEN);
        else
            view.setBackgroundColor(Color.TRANSPARENT);

        return songLay;
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

    public ArrayList<Song> getNewPlaylist(){
        ArrayList<Song> newPlaylist=new ArrayList<Song>();
        for(int pos:coloredItems){
            Song currSong=songs.get(pos);
            newPlaylist.add(currSong);
        }
        return newPlaylist;
    }


}
