package com.a700apps.ummelquwain.ui.screens.landing.more.events;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;
import com.a700apps.ummelquwain.models.response.Events.EventsModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.details.EventDetailsFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class EventsPresenter implements EventsContract.UserAction, LifecycleObserver {

    private Context mContext;
    private EventsContract.View mView;
    private FragmentManager mFragmentManager;
    private Call<EventsModel> mEventsCall;
    private List<EventResultModel> mModel;

    public EventsPresenter(Context mContext, EventsContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mEventsCall = MyApplication.get(mContext).getApiService()
                .getAllEvents(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mEventsCall.enqueue(new Callback<EventsModel>() {
            @Override
            public void onResponse(@NonNull Call<EventsModel> call, @NonNull Response<EventsModel> response) {
                mModel = response.body().getResult();
                mView.hideProgress();
                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<EventsModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(EventResultModel event) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, EventDetailsFragment.newInstance(event)).commit();
    }
}
