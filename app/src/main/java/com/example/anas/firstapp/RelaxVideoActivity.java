package com.example.anas.firstapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

public class RelaxVideoActivity extends BaseActivity {

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        video = (VideoView) findViewById(R.id.video);
        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_relaxation);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.relax_carole_serrat;
        video.setVideoURI(Uri.parse(path));
        video.start();

        MediaController mc = new MediaController(this);

        mc.setMediaPlayer(video);
        video.setMediaController(mc);

        findViewById(R.id.passer_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ProfilesActivity.class);
            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_relaxvideo;
    }
}
