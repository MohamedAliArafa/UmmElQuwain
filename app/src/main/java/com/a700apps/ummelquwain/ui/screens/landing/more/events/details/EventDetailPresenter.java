package com.a700apps.ummelquwain.ui.screens.landing.more.events.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.EventRequestModel;
import com.a700apps.ummelquwain.models.response.Events.EventModel;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 9/7/2017.
 */

public class EventDetailPresenter implements EventDetailContract.UserAction, LifecycleObserver {

    private Context mContext;
    private EventDetailContract.ModelView mView;
    private Call<EventModel> mEventCall;
    private EventResultModel mModel;
    private int mEventID;

    public EventDetailPresenter(Context mContext, EventDetailContract.ModelView mView, Lifecycle lifecycle, int mEventID) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mEventID = mEventID;
    }
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mEventCall = MyApplication.get(mContext).getApiService()
                .getEventDetails(new EventRequestModel(MyApplication.get(mContext).getLanguage(), mEventID));
        mEventCall.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(@NonNull Call<EventModel> call, @NonNull Response<EventModel> response) {
                try {
                    mModel = response.body().getResult();
                    mView.updateUI(mModel);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<EventModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
