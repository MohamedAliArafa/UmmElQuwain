package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos.details.YoutubeFragment;

public class YouTubeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        YoutubeFragment fragment = new YoutubeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}
