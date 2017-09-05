package com.a700apps.ummelquwain.ui.screens.landing.stations;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationsPresenter implements StationsContract.UserAction, LifecycleObserver {
    private Context mContext;
    private StationsContract.View mView;
    private FragmentManager mFragmentManager;
    private Call<StationsModel> mStationsCall;
    private List<StationResultModel> mModel;
    private Realm mRealm;

    public StationsPresenter(Context mContext, StationsContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }


    @Override
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService().getAllStations(new StationsRequestModel(1, -1));
        mStationsCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();

                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int stationID) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, StationFragment.newInstance(stationID)).commit();
    }
}
