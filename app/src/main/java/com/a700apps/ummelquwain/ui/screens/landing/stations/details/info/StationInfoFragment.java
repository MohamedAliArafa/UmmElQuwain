package com.a700apps.ummelquwain.ui.screens.landing.stations.details.info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationInfoFragment extends Fragment {


    public StationInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station_info, container, false);
    }

}
