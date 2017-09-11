package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos;

import android.os.Bundle;
import android.widget.Toast;

import com.a700apps.ummelquwain.BuildConfig;
import com.a700apps.ummelquwain.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class YouTubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);
    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("nCgQDjiotG0");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }
}
