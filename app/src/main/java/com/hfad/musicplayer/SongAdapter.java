package com.hfad.musicplayer;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Rajeev on 10-01-2017.
 */

public class SongAdapter extends BaseAdapter{                                                       //This class inflates the xml
                                                                                                    //and makes it drawable as a
    private ArrayList<Song> songs;                                                                  //as a view in relation to the
    private LayoutInflater songInf;
    Context cont;



                                                                                                    //entire list. The adapter class
    public SongAdapter(Context c, ArrayList<Song> theSongs){                                        //does this automatically
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
       // View view = convertView;
        //final int pos = position;
        //final ArrayList<Integer> coloredItems=new ArrayList<Integer>();

        /*if(view == null){

            final View viewClick = view;

            LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.song, null);

        }*/
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
        songLay.setTag(position);//This tag is used to start service
        /*songView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED POSITION",Integer.valueOf(pos).toString());
                View parent = (View) v.getParent();



                parent.setBackgroundColor(Color.GREEN);
                coloredItems.add(Integer.valueOf(pos));
            }
        });
        if(coloredItems.contains(Integer.valueOf(position)))
            view.setBackgroundColor(Color.GREEN);
        else
            view.setBackgroundColor(Color.TRANSPARENT);*/

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









}
