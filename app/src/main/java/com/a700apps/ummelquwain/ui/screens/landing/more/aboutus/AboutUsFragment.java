package com.a700apps.ummelquwain.ui.screens.landing.more.aboutus;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsResultModel;
import com.a700apps.ummelquwain.utilities.SwipeToDismissHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements
        View.OnClickListener, AboutUsContract.View, LifecycleRegistryOwner{

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_manager_message)
    TextView mMangerMessage;

    @BindView(R.id.tv_manager_name)
    TextView mMangerName;

    @BindView(R.id.tv_vision)
    TextView mVision;

    @BindView(R.id.tv_mission)
    TextView mMission;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    GestureDetector mGesture;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    AboutUsPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        setupView(view);
        return view;
    }

    void setupView(View view) {
        mPresenter = new AboutUsPresenter(this, getContext(), getLifecycle());
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
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
    public void updateUI(AboutUsResultModel mModel) {
        mMangerMessage.setText(mModel.getUBNManagerWord());
        mMangerName.setText(mModel.getMangerName());
        mVision.setText(mModel.getVision());
        mMission.setText(mModel.getMission());
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
