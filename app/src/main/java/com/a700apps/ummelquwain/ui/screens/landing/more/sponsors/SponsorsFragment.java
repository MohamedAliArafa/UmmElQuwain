package com.a700apps.ummelquwain.ui.screens.landing.more.sponsors;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.SponsorAdapter;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.utilities.SwipeToDismissHelper;
import com.a700apps.ummelquwain.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SponsorsFragment extends Fragment implements View.OnClickListener, SponsorsContract.View, LifecycleRegistryOwner {

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private GestureDetector mGesture;
    private SponsorAdapter mAdapter;
    private SponsorPresenter mPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public SponsorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SponsorPresenter(getContext(), this, getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sponsors, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        RecyclerView rv = view.findViewById(R.id.recycler);
        int spanCount = Utility.calculateNoOfColumns(getContext(), 100);
        rv.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mAdapter = new SponsorAdapter(getContext(), null, R.layout.list_item_sponser);
        rv.setAdapter(mAdapter);
        mGesture = new GestureDetector(getActivity(),
                new SwipeToDismissHelper(getFragmentManager()));
        view.setOnTouchListener((v, event) -> mGesture.onTouchEvent(event));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateUI(List<SponsorResultModel> mModel) {
        mAdapter.updateData(mModel);
    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
