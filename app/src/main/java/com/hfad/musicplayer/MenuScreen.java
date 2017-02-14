package com.hfad.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Rajeev on 27-01-2017.
 */

public class MenuScreen extends AppCompatActivity{
    Button allSongs;
    Button playlist;
    private final int PERMS_REQUEST_CODE=123;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        if(hasPerms()){
            onCreatePerms();

        }else {
            requestPerms();
        }

    }

    private void onCreatePerms(){                                                // This block comes in the oncreate if permission
                                                                                // is given
        allSongs=(Button) findViewById(R.id.allSongs);
        playlist=(Button)findViewById(R.id.playlist);
        allSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main=new Intent(getApplicationContext() , MainActivity.class);
                startActivity(main);
            }
        });
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playlist=new Intent(MenuScreen.this,PlaylistScreenActivity.class);
                startActivity(playlist);
            }
        });

    }                                                                           //Block ends

    private boolean hasPerms(){                                                                     //Related to permissions
        //Checking if permission
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }
    private void requestPerms(){                                                                    //Related to permissions
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};             //Requesting permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,          //Overriding the request permission
                                           @NonNull int[] grantResults) {                           //method to include our code.
        boolean allowed = true;

        switch (requestCode){
            case PERMS_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }
        if(allowed) {
            onCreatePerms();

        }else{
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }                                                                                               //Permission block ends.
}

