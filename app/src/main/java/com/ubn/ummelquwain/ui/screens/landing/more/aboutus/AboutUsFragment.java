package com.ubn.ummelquwain.ui.screens.landing.more.aboutus;


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

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.AboutUs.AboutUsResultModel;
import com.ubn.ummelquwain.utilities.Constants;
import com.ubn.ummelquwain.utilities.SwipeToDismissHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements
        View.OnClickListener, AboutUsContract.ModelView, LifecycleRegistryOwner {

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_manager_message)
    TextView mMangerMessage;

    @BindView(R.id.iv_manager_image)
    ImageView mMangerImage;

    @BindView(R.id.tv_manager_name)
    TextView mMangerName;

    @BindView(R.id.tv_vision)
    TextView mVision;

    @BindView(R.id.tv_mission)
    TextView mMission;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    private GestureDetector mGesture;
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
        switch (viewId) {
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
    public void updateUI(AboutUsResultModel model) {
        mMangerMessage.setText(model.getUBNManagerWord());
        mMangerName.setText(model.getMangerName());
        mVision.setText(model.getVision());
        mMission.setText(model.getMission());
        GlideApp.with(this)
                .load(model.getUBNManagerImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                .fitCenter()
                .into(mMangerImage);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
