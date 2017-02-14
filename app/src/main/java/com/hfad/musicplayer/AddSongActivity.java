package com.hfad.musicplayer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Rajeev on 03-02-2017.
 */

public class AddSongActivity extends AppCompatActivity {
    ListView songView;
    ArrayList<Song> songList;
    Button addToPlaylist;
    AddSongAdapter songAdt;
    Playlist newPlaylist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_song_screen);
        songView = (ListView) findViewById(R.id.addSong_list);
        addToPlaylist=(Button) findViewById(R.id.addToPlaylist);
        songList = new ArrayList<Song>();
        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        songAdt = new AddSongAdapter(this, songList);
        songView.setAdapter(songAdt);
        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder= new AlertDialog.Builder(AddSongActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.alert_box,null);
                final EditText playlistName=(EditText)mView.findViewById(R.id.playlistName);
                final Button savePlaylist=(Button) mView.findViewById(R.id.savePlaylist);
                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();
                dialog.show();
                savePlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!playlistName.getText().toString().isEmpty()){
                            newPlaylist=new Playlist(playlistName.getText().toString(),songAdt.getNewPlaylist());
                            dialog.dismiss();
                            Intent intent =new Intent(AddSongActivity.this,PlaylistScreenActivity.class);
                            intent.putExtra("newPlaylist",newPlaylist);
                            startActivity(intent);

                        }else{
                            Toast.makeText(AddSongActivity.this,"Please enter a name!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }


    public void getSongList(){                                                                      //Using content resolver class
        ContentResolver musicResolver = getContentResolver();                                       //to get songs from the user'
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;                //phone.
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
    public void songPicked(View view){

    }
}
