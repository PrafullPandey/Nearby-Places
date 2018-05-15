package com.example.p2_vaio.nearbysearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by p2_vaio on 7/8/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolBar(boolean enableHome){
        Log.d(TAG, "activateToolBar: start");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar==null){
            actionBar = getSupportActionBar();/*
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            if(toolbar!=null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }*/
        }
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }

    }

}
