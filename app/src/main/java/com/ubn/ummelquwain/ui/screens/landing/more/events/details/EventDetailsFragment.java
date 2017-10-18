package com.ubn.ummelquwain.ui.screens.landing.more.events.details;


import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.Events.EventResultModel;
import com.ubn.ummelquwain.ui.screens.landing.more.events.EventsContract;
import com.ubn.ummelquwain.ui.screens.landing.more.events.EventsPresenter;
import com.ubn.ummelquwain.utilities.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ubn.ummelquwain.utilities.Constants.REQUEST_READ_CALENDER_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements EventDetailContract.ModelView, View.OnClickListener, LifecycleOwner, EventsContract.ModelView {

    private static EventResultModel mModel;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;
    @BindView(R.id.iv_event_image)
    ImageView mEventImageView;

    @BindView(R.id.btn_share)
    FrameLayout mShareBtn;
    @BindView(R.id.btn_add_calender)
    FrameLayout mAddCalenderBtn;

    @BindView(R.id.tv_event_title)
    TextView mEventTitleTextView;
    @BindView(R.id.tv_event_date)
    TextView mEventDateTextView;
    @BindView(R.id.tv_event_location)
    TextView mEventLocationTextView;
    @BindView(R.id.tv_event_desc)
    TextView mEventDescTextView;

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    EventsPresenter mPresenter;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_event, container, false);
        mPresenter = new EventsPresenter(getContext(), this, getFragmentManager(), getLifecycle());
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mAddCalenderBtn.setOnClickListener(this);
        showProgress();
        updateUI(mModel);
        return view;
    }

    public static EventDetailsFragment newInstance(EventResultModel event) {
        mModel = event;
        return new EventDetailsFragment();
    }

    @Override
    public void updateUI(EventResultModel model) {
        mEventTitleTextView.setText(model.getEventName());
        String formattedDate = String.format(getString(R.string.title_from_to), model.getEventStartDate(), model.getEventEndDate());
        mEventDateTextView.setText(formattedDate);
        mEventLocationTextView.setText(model.getEventPlace());
        mEventDescTextView.setText(model.getEventDescription());
        GlideApp.with(this)
                .load(model.getEventImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                .fitCenter()
                .into(mEventImageView);
        hideProgress();
    }

    @Override
    public void updateUI(List<EventResultModel> models) {

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
    public boolean requestReadPermission() {
        if (CheckPermission(this.getActivity(), Manifest.permission.WRITE_CALENDAR)) {
            return true;
        } else {
            RequestPermission(this.getActivity(), Manifest.permission.WRITE_CALENDAR, REQUEST_READ_CALENDER_PERMISSION);
            return false;
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity, Permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{Permission}, Code);
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_share:
                mPresenter.shareEvent(mModel);
                break;
            case R.id.btn_add_calender:
                mPresenter.addToCalender(mModel);
                break;
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
