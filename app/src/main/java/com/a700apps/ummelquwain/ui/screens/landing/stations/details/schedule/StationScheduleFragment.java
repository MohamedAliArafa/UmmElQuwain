package com.a700apps.ummelquwain.ui.screens.landing.stations.details.schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.ScheduleNestedAdapter;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationScheduleFragment extends Fragment {

    private static StationResultModel mStation;
    @BindView(R.id.recycler_schedule_main)
    RecyclerView mRecycler;
    private ScheduleNestedAdapter mAdapter;

    public StationScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station_schedule, container, false);
        ButterKnife.bind(this, view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScheduleNestedAdapter(getContext(), mStation.getSchedule(), R.layout.list_item_days_schedule);
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    public static StationScheduleFragment newInstance(StationResultModel station) {
        mStation = station;
        return new StationScheduleFragment();
    }
}
