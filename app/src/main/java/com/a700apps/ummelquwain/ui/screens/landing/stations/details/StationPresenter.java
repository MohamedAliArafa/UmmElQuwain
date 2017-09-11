package com.a700apps.ummelquwain.ui.screens.landing.stations.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.StationDetailsRequestModel;
import com.a700apps.ummelquwain.models.response.Station.StationModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationPresenter implements StationContract.UserAction, LifecycleObserver {
    private Context mContext;
    private StationContract.View mView;
    private Call<StationModel> mStationCall;
    private StationResultModel mModel;
    private Realm mRealm;
    private int mStationID;

    public StationPresenter(Context mContext,int mStationID, StationContract.View mView, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mStationID = mStationID;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        StationResultModel query = mRealm.where(StationResultModel.class).equalTo("stationID", mStationID).findFirst();
        if (query != null){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
            mView.setupViewPager();
            mView.setupTabLayout();
        }
        mStationCall = MyApplication.get(mContext).getApiService()
                .getStationDetails(
                        new StationDetailsRequestModel(MyApplication.get(mContext).getLanguage(),
                                MyApplication.get(mContext).getUser(), mStationID));
        mStationCall.enqueue(new Callback<StationModel>() {
            @Override
            public void onResponse(@NonNull Call<StationModel> call, @NonNull Response<StationModel> response) {
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();

                mView.updateUI(mModel);

                mView.setupViewPager();
                mView.setupTabLayout();

            }

            @Override
            public void onFailure(@NonNull Call<StationModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
