package com.a700apps.ummelquwain.ui.screens.landing.stations;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.request.SearchRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;
import com.a700apps.ummelquwain.ui.screens.LoginFragment;
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
    private Call<MessageModel> mFavCall;
    private List<StationResultModel> mModel;
    private Realm mRealm;

    public StationsPresenter(Context mContext, StationsContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .getAllStations(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), MyApplication.get(mContext).getUser()));
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

    @Override
    public void search(String keyword) {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .searchStations(new SearchRequestModel(keyword, MyApplication.get(mContext).getUser(), MyApplication.get(mContext).getLanguage()));
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
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void setFav(int itemID, int isFav) {
        String user = ((MyApplication) mContext.getApplicationContext()).getUser();
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
                    mView.hideProgress();
                }

                @Override
                public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    mView.hideProgress();
                }
            });
        }
    }

    @Override
    public void openLogin() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null).commit();
    }
}
