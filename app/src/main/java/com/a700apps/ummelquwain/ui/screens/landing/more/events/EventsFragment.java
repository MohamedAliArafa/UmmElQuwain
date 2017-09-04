package com.a700apps.ummelquwain.ui.screens.landing.more.events;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.EventsAdapter;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;
import com.a700apps.ummelquwain.utilities.SwipeToDismissHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements View.OnClickListener, EventsContract.View, LifecycleRegistryOwner{


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    private GestureDetector mGesture;
    private EventsAdapter mAdapter;
    private EventsPresenter mPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @BindView(R.id.recycler)
    RecyclerView mRecycler;


    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new EventsPresenter(getContext(), this, getFragmentManager(), getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new EventsAdapter(null, R.layout.list_item_events, mPresenter);
        mRecycler.setAdapter(mAdapter);
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
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void updateUI(List<EventResultModel> model) {
        mAdapter.updateData(model);
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
