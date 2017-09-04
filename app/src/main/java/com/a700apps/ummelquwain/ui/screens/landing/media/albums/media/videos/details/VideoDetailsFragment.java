package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDetailsFragment extends Fragment implements VideosDetailContract.View {


    public VideoDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_video, container, false);
    }

    public static VideoDetailsFragment newInstance(int mediaID) {
        return new VideoDetailsFragment();
    }
}
