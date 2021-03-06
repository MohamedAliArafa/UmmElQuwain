package com.ubn.ummelquwain.ui.screens.landing.stations.details.info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.adapter.StationProgramsAdapter;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationInfoFragment extends Fragment {

    private static StationResultModel mModel;
    @BindView(R.id.tv_info_desc)
    TextView mInfoDescTextView;
    @BindView(R.id.tv_info_site)
    TextView mInfoSiteTextView;
    @BindView(R.id.tv_info_freq)
    TextView mInfoFreqTextView;
    @BindView(R.id.tv_info_language)
    TextView mInfoLanguageTextView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecycler;

    private StationProgramsAdapter mAdapter;
    private StationInfoPresenter mPresenter;

    public StationInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station_info, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new StationInfoPresenter(getContext(), getParentFragment().getFragmentManager());
        updateUI();
        return view;
    }

    public static StationInfoFragment newInstance(StationResultModel model) {
        mModel = model;
        return new StationInfoFragment();
    }

    void updateUI() {
        mInfoDescTextView.setText(mModel.getStationInfo());
        mInfoFreqTextView.setText(mModel.getStationFrequency());
        mInfoLanguageTextView.setText(mModel.getStationLanguage());
        mInfoSiteTextView.setText(mModel.getStationWebsite());
        mInfoSiteTextView.setOnClickListener(view -> {
            mPresenter.openSite(mModel.getStationWebsite());
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StationProgramsAdapter(mModel.getPrograms(), R.layout.list_item_days_schedule, mPresenter);
        mRecycler.setAdapter(mAdapter);
    }
}
