package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ubn.ummelquwain.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {

    @BindView(R.id.video_player_view)
    VideoView mVideoPlayer;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this mediaVideoFragment
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, view);
        MediaController mediaController = new MediaController(getActivity());
        mVideoPlayer.setVideoURI(Uri.parse(getArguments().getString("video_url")));
        mVideoPlayer.setMediaController(mediaController);
        mVideoPlayer.start();
        return view;
    }

}
