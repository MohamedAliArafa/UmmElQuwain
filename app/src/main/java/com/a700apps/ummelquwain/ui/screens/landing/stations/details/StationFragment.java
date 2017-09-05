package com.a700apps.ummelquwain.ui.screens.landing.stations.details;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements LifecycleRegistryOwner{

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public StationFragment() {
        // Required empty public constructor
    }

    public static StationsFragment newInstance(int stationID) {
        return new StationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station, container, false);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
