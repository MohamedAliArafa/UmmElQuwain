package com.a700apps.ummelquwain.ui.screens.landing.programs.details.info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.ProgramScheduleAdapter;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramInfoFragment extends Fragment {

    private static ProgramResultModel mModel;

    @BindView(R.id.tv_program_info_dec)
    TextView mProgramInfoTextView;

    @BindView(R.id.recycler_program_schedule)
    RecyclerView mScheduleRecycler;
    private ProgramScheduleAdapter mAdapter;

    public ProgramInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_info, container, false);
        ButterKnife.bind(this, view);
        mScheduleRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProgramScheduleAdapter(getContext(), null, R.layout.list_item_program_schedule);
        mScheduleRecycler.setAdapter(mAdapter);
        updateView();
        return view;
    }

    private void updateView() {
        mProgramInfoTextView.setText(mModel.getProgramInfo());
        mAdapter.updateData(mModel.getSchedule());
    }

    public static ProgramInfoFragment newInstance(ProgramResultModel model) {
        mModel = model;
        return new ProgramInfoFragment();
    }
}
