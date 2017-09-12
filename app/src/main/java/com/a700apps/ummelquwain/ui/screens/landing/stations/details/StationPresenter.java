package com.a700apps.ummelquwain.ui.screens.landing.stations.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.request.StationDetailsRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationPresenter implements StationContract.UserAction, LifecycleObserver {
    private final FragmentManager mFragmentManager;
    private final Lifecycle mLifeCycle;
    private Context mContext;
    private StationContract.View mView;
    private Call<StationModel> mStationCall;
    private StationResultModel mModel;
    private Realm mRealm;
    private int mStationID;
    private int favStatus;
    private Call<MessageModel> mFavCall;

    public StationPresenter(Context mContext, int mStationID, FragmentManager mFragmentManager
            , StationContract.View mView, Lifecycle mLifeCycle) {
        this.mLifeCycle = mLifeCycle;
        this.mLifeCycle.addObserver(this);
        this.mFragmentManager = mFragmentManager;
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
        if (query != null) {
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

                if (!mLifeCycle.getCurrentState().isAtLeast(Lifecycle.State.DESTROYED))
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

        mModel.addChangeListener(stationResultModels -> {
            if (!mLifeCycle.getCurrentState().isAtLeast(Lifecycle.State.DESTROYED))
                mView.updateUI(mModel);
        });
    }

    @Override
    public void openLogin() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment(), "login_fragment")
                .addToBackStack(null).commit();
    }

    @Override
    public void setFav(int itemID, int isFav, StationContract.adapterCallback callback) {
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
}
