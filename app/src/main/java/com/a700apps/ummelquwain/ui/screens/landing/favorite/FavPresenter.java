package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
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
 * Created by mohamed.arafa on 9/5/2017.
 */

public class FavPresenter implements FavContract.UserAction, LifecycleObserver {

    private Context mContext;
    private FavContract.View mView;
    private FragmentManager mFragmentManager;
    private Call<StationsModel> mStationCall;
    private List<StationResultModel> mStationModel;

    private Call<SponsorModel> mSponsorCall;
    private List<SponsorResultModel> mSponsorModel;

    private Realm mRealm;

    public FavPresenter(Context mContext, FavContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getStationData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mStationModel = query;
            mView.hideProgress();
            mView.updateFavUI(mStationModel);
        }
        mStationCall = MyApplication.get(mContext).getApiService().getAllStations(new StationsRequestModel(1, -1));
        mStationCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
                mStationModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mStationModel);
                mRealm.commitTransaction();

                mView.updateFavUI(mStationModel);
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getSponsorData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<SponsorResultModel> query = mRealm.where(SponsorResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mSponsorModel = query;
            mView.hideProgress();
            mView.updateSponsorUI(mSponsorModel);
        }
        mSponsorCall = MyApplication.get(mContext).getApiService().getAllSponsors(new LanguageRequestModel(1));
        mSponsorCall.enqueue(new Callback<SponsorModel>() {
            @Override
            public void onResponse(@NonNull Call<SponsorModel> call, @NonNull Response<SponsorModel> response) {
                mSponsorModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mStationModel);
                mRealm.commitTransaction();

                mView.updateSponsorUI(mSponsorModel);
            }

            @Override
            public void onFailure(@NonNull Call<SponsorModel> call, @NonNull Throwable t) {
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
