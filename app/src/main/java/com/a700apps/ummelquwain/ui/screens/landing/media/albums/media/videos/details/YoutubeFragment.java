package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.BuildConfig;
import com.a700apps.ummelquwain.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    // YouTubeのビデオID
    private static String VIDEO_ID = "EGy39OMyHzw";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerSupportFragment mYouTubePlayerFragment;

    public static YoutubeFragment newInstance(String videoID) {
        VIDEO_ID = videoID;
        return new YoutubeFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        mYouTubePlayerFragment.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube, container, false);

        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, mYouTubePlayerFragment).commit();

        mYouTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);
        return rootView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            player.loadVideo(VIDEO_ID);
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            player.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
        // YouTube error
        error.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST);
//                String errorMessage = error.toString();
//                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
//                Log.d("errorMessage:", errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            mYouTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);
        }
    }
}