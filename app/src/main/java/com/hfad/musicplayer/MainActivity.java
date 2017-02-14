package com.hfad.musicplayer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import android.widget.Toast;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
import com.hfad.musicplayer.MusicService.MusicBinder;
import android.widget.AdapterView.OnItemClickListener;



public class MainActivity extends AppCompatActivity implements MediaPlayerControl {

    private ArrayList<Song> songList;
    private ListView songView;
    private Intent playIntent;
    private boolean musicBound;
    private MusicService musicSrv;
    private boolean paused = false, playbackPaused = false;
    MusicController controller;
    private SongAdapter songAdt;
    private int pos;
    private TextView songName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView=(ListView)findViewById(R.id.song_list);
        songName=(TextView)findViewById(R.id.tvSongPlaying);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            songList=extras.getParcelableArrayList("songList");

        }else {
            songList = new ArrayList<Song>();
            getSongList();
            Collections.sort(songList, new Comparator<Song>() {
                public int compare(Song a, Song b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
        }
        songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        setController();



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
        int index=Integer.parseInt(view.getTag().toString());
        musicSrv.setSong(index);
        controller.show();
        musicSrv.pausePlayer();
        musicSrv.playSong();
        songName.setText(musicSrv.getSong().getTitle().toString());
    }

    private void setController() {
        if (controller == null)controller = new MusicController(this, findViewById(R.id.frame_layout));

        //set the controller up
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setEnabled(true);


    }

    private void playNext() {
        musicSrv.playNext();
        songName.setText(musicSrv.getSong().getTitle().toString());
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);


    }

    private void playPrev() {
        musicSrv.playPrev();
        songName.setText(musicSrv.getSong().getTitle().toString());
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }


    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }


    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up receiver for media player onPrepared broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(onPrepareReceiver,
                new IntentFilter("MEDIA_PLAYER_PREPARED"));
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            //shuffle
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                Intent intent=new Intent(MainActivity.this,MenuScreen.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {                            //Establishing connection
        //with the service which plays
        @Override
        //the music
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {                                                                       //Using intent to connect
            playIntent = new Intent(this, MusicService.class);                                      //service class and start
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);                     //the service
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    // Broadcast receiver to determine when music player has been prepared
    private BroadcastReceiver onPrepareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            // When music player has been prepared, show controller
            controller.show(0);
        }
    };




}
