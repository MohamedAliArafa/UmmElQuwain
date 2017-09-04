package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.images.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends Fragment {


    public ImageDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_image, container, false);
    }

    public static ImageDetailsFragment newInstance(int mediaID) {
        return new ImageDetailsFragment();
    }
}
