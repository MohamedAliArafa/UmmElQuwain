package com.a700apps.ummelquwain.ui.screens.landing.stations;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.StationAdapter;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.utilities.ClickableEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StationsFragment extends Fragment implements StationsContract.View, LifecycleRegistryOwner {

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    @BindView(R.id.recycler_stations)
    RecyclerView mRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.et_search)
    ClickableEditText mSearchEditText;

    StationAdapter mAdapter;
    StationsPresenter mPresenter;

    public StationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StationsPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle());
    }

    public static StationsFragment newInstance() {
        return new StationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        ButterKnife.bind(this, view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StationAdapter(getContext(), null, R.layout.list_item_station, mPresenter);
        mRecycler.setAdapter(mAdapter);
        setupSearch();
        return view;
    }

    void setupSearch() {
        mSearchEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_search), null);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    mPresenter.getData();
            }
        });
        mSearchEditText.setDrawableClickListener(target -> {
            switch (target) {
                case RIGHT:
                    //Do something here
                    if (!mSearchEditText.getText().toString().isEmpty())
                        mPresenter.search(mSearchEditText.getText().toString());
                    break;

                default:
                    break;
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void updateUI(List<StationResultModel> models) {
        mAdapter.updateData(models);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
