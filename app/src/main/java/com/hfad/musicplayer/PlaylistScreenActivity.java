package com.hfad.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rajeev on 03-02-2017.
 */

public class PlaylistScreenActivity extends AppCompatActivity {
    private ArrayList<Playlist> playlistList;
    private ListView playlistView;
    private PlaylistScreenAdapter playAdt;
    private Button addNewPlaylist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_screen);
        playlistView=(ListView)findViewById(R.id.playlist_list);
        addNewPlaylist=(Button)findViewById(R.id.addNewPlaylist);
        Bundle extras=getIntent().getExtras();
        playlistList=getPlaylistList();
        if(extras!=null){
            Playlist newPlaylist;
            newPlaylist=extras.getParcelable("newPlaylist");
            playlistList.add(newPlaylist);
            Collections.sort(playlistList, new Comparator<Playlist>(){                              //Sorting it again
                public int compare(Playlist a, Playlist b){
                    return a.getPlaylistName().compareTo(b.getPlaylistName());
                }
            });
            setPlaylistList();                                                          //set into memory

        }
        if(playlistList!=null) {
            playAdt = new PlaylistScreenAdapter(this, playlistList);
            playlistView.setAdapter(playAdt);
        }

        addNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaylistScreenActivity.this,AddSongActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onClickPlaylist(View view){
        int pos=Integer.parseInt(view.getTag().toString());
        Playlist selectPlaylist;
        selectPlaylist=playlistList.get(pos);
        Intent intent=new Intent(PlaylistScreenActivity.this,MainActivity.class);
        intent.putExtra("songList",selectPlaylist.getPlaylist());
        startActivity(intent);
    }
    public ArrayList<Playlist> getPlaylistList(){
        playlistList=new ArrayList<>();
        SharedPreferences appSharedPrefs=PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson=new Gson();
        String json=appSharedPrefs.getString("myPlaylistList","");
        Type type=new TypeToken<ArrayList<Playlist>>(){}.getType();//get the arraylist from memory
        if(gson.fromJson(json,type)!=null){
            playlistList=gson.fromJson(json,type);                             //assign it to the initialized arraylist
        }
        return playlistList;                                                //return that arraylist
    }
    public void setPlaylistList(){
        SharedPreferences appSharedPrefs= PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor=appSharedPrefs.edit();
        Gson gson=new Gson();
        String json=gson.toJson(playlistList);
        prefsEditor.putString("myPlaylistList",json);
        prefsEditor.commit();
    }


}
