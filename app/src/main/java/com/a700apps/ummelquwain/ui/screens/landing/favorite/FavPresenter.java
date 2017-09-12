package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsContract;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;

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
    private RealmResults<StationResultModel> mStationModel;

    private Call<SponsorModel> mSponsorCall;
    private List<SponsorResultModel> mSponsorModel;

    private Realm mRealm;
    private Call<MessageModel> mFavCall;
    private int favStatus   ;

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
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).equalTo("isFavourite", 1).findAll();
        mStationModel = query;
        if (query.isLoaded() && !query.isEmpty()){
            mView.hideProgress();
            mView.updateFavUI(mStationModel);
        }
        mStationCall = MyApplication.get(mContext).getApiService()
                .getFavStations(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), MyApplication.get(mContext).getUser()));
        mStationCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
                mView.hideProgress();
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(response.body().getResult());
                mRealm.commitTransaction();
//                mView.updateFavUI(mStationModel);
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                mView.hideProgress();
            }
        });

        mStationModel.addChangeListener(stationResultModels -> {
            mView.updateFavUI(mStationModel);
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
        mSponsorCall = MyApplication.get(mContext).getApiService()
                .getAllSponsors(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
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

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void setFav(int itemID, int isFav, StationsContract.adapterCallback callback) {
        String user = ((MyApplication) mContext.getApplicationContext()).getUser();
        favStatus = isFav;
        if (!user.equals("-1")) {
            mView.showProgress();
            if (isFav == 0)
                mFavCall = MyApplication.get(mContext).getApiService()
                        .addTofav(new FavouriteRequestModel(user, 1, itemID));
            else
                mFavCall = MyApplication.get(mContext).getApiService()
                        .removeFromfav(new FavouriteRequestModel(user, 0, itemID));
            mFavCall.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                    MessageResultModel model = response.body().getResult();
                    if (model.getSuccess())
                        callback.favCallback(favStatus == 1 ? 0 : 1);
                    mView.hideProgress();
                }

                @Override
                public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    callback.favCallback(favStatus);
                    mView.hideProgress();
                }
            });
        }
    }

    @Override
    public void openDetails(int stationID) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, StationFragment.newInstance(stationID)).commit();
    }

    @Override
    public void openLogin() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment(), "login_fragment")
                .addToBackStack(null).commit();
    }
}
