package com.hfad.musicplayer;

import android.content.Context;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by Rajeev on 15-01-2017.
 */


    public class MusicController extends MediaController {

        public MusicController(Context c, View Anchor){
            super(c);
            super.setAnchorView(Anchor);
        }
        @Override
        public void setAnchorView(View view){

        }
        public void hide(){}

    }

