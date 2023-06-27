package com.android.example.shiftmanagementapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.shiftmanagementapp.R;

public class HelpActivity extends AppCompatActivity {

    private VideoView _video;

    private ImageButton _startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _video = findViewById(R.id.videoView);

        // Set video file path
        _video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.help_try);

        // Set media controller to enable controls like play, pause, seek
        MediaController mediaController = new MediaController(this);
        _video.setMediaController(mediaController);
        mediaController.setAnchorView(_video);


        _startButton = findViewById(R.id.StartButton);
        _startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _startButton.setVisibility(View.INVISIBLE);
                _video.setVisibility(View.VISIBLE);
                _video.start();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}