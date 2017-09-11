package com.a700apps.ummelquwain.ui.screens.landing.programs.details.commnts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.CommentsAdapter;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramCommentsFragment extends Fragment {

    private static ProgramResultModel mModel;
    @BindView(R.id.recycler_comments)
    RecyclerView mRecycler;

    private CommentsAdapter mAdapter;

    public ProgramCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_commenrs, container, false);
        ButterKnife.bind(this, view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommentsAdapter(getContext(), mModel.getUserComments(), R.layout.list_item_comment);
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    public static ProgramCommentsFragment newInstance(ProgramResultModel model) {
        mModel = model;
        return new ProgramCommentsFragment();
    }
}
