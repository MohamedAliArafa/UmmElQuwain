package com.a700apps.ummelquwain.ui.screens.landing.more.events.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements EventDetailContract.View, View.OnClickListener {

    private static EventResultModel mModel;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.tv_event_title)
    TextView mEventTitleTextView;
    @BindView(R.id.tv_event_date)
    TextView mEventDateTextView;
    @BindView(R.id.tv_event_location)
    TextView mEventLocationTextView;
    @BindView(R.id.tv_event_desc)
    TextView mEventDescTextView;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_event, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
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
        String formattedDate = String.format("From: %s \t To: %s", model.getEventStartDate(), model.getEventEndDate());
        mEventDateTextView.setText(formattedDate);
        mEventLocationTextView.setText(model.getEventPlace());
        mEventDescTextView.setText(model.getEventDescription());
        hideProgress();
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
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }
}
