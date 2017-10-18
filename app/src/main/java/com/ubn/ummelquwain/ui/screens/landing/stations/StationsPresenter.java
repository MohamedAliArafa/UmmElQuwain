package com.ubn.ummelquwain.ui.screens.landing.stations;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.request.FavouriteRequestModel;
import com.ubn.ummelquwain.models.request.StationsRequestModel;
import com.ubn.ummelquwain.models.response.Message.MessageModel;
import com.ubn.ummelquwain.models.response.Message.MessageResultModel;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.models.response.Station.StationsModel;
import com.ubn.ummelquwain.player.Player;
import com.ubn.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.ubn.ummelquwain.ui.screens.login.LoginFragment;
import com.ubn.ummelquwain.utilities.DetailsTransition;
import com.ubn.ummelquwain.utilities.Utility;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ubn.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationsPresenter implements StationsContract.UserAction, LifecycleObserver, Callback<StationsModel> {
    private Context mContext;
    private StationsFragment mView;
    private FragmentManager mFragmentManager;
    private Call<StationsModel> mStationsCall;
    private Call<MessageModel> mFavCall;
    private RealmResults<StationResultModel> mModel;
    private Realm mRealm;
    private Player mPlayer;
    private boolean playing;

    public StationsPresenter(Context mContext, StationsFragment mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        mPlayer = MyApplication.get(mContext).getPlayer();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        mModel = query;
        if (query.isLoaded() && !query.isEmpty()) {
            mView.hideProgress();
            mView.updateUI(mModel);
            mModel.addChangeListener(stationResultModels -> mView.updateUI(mModel));
        }
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = deviceId;
        mStationsCall = MyApplication.get(mContext).getApiService()
                .getAllStations(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), user));
        mStationsCall.enqueue(this);
    }

    @Override
    public void openDetails(int stationID, View viewShared) {
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
                PROGRAM_FRAGMENT_KEY + String.valueOf(stationID), 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            StationFragment fragment = StationFragment.newInstance(stationID);
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addSharedElement(viewShared, viewShared.getTransitionName());
            ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(stationID));
            ft.commit();
        }
    }

    @Override
    public void playStream(StationResultModel station) {
        mPlayer.playStream(station);
    }

    @Override
    public void search(String keyword) {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class)
                .contains("stationName", keyword, Case.INSENSITIVE)
                .findAll();
        if (query.isLoaded() && query.isEmpty()) {
            query = mRealm.where(StationResultModel.class)
                    .contains("categoryName", keyword, Case.INSENSITIVE)
                    .findAll();
            if (query.isLoaded() && query.isEmpty())
                query = mRealm.where(StationResultModel.class)
                        .contains("stationLanguage", keyword, Case.INSENSITIVE)
                        .findAll();
        }
        if (query.isLoaded()) {
            mView.hideProgress();
            mModel = query;
            mView.updateUI(mModel);
        }
    }

    private int favStatus;

    @Override
    public void setFav(int itemID, int isFav, StationsContract.adapterCallback callback) {
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = null;
        if (deviceId == null) {
            return;
        }
        favStatus = isFav;

        mView.showProgress();
        if (isFav == 0)
            mFavCall = MyApplication.get(mContext).getApiService()
                    .addTofav(new FavouriteRequestModel(user, deviceId, 1, itemID));
        else
            mFavCall = MyApplication.get(mContext).getApiService()
                    .removeFromfav(new FavouriteRequestModel(user, deviceId, 1, itemID));
        mFavCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                try {
                    MessageResultModel model = response.body().getResult();
                    if (model.getSuccess())
                        callback.favCallback(favStatus == 1 ? 0 : 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    @Override
    public void openLogin() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment(), "login_fragment")
                .addToBackStack(null).commit();
    }

    @Override
    public void onResponse(Call<StationsModel> call, Response<StationsModel> response) {
        try {
            Trace.beginSection("Data structures");
            Utility.addStationsToRealm(response.body().getResult(), () -> {
                mModel = mRealm.where(StationResultModel.class).findAll();
                mModel.addChangeListener(stationResultModels -> mView.updateUI(mModel));
                mView.updateUI(mModel);
            });
            Trace.endSection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView.hideProgress();
    }

    @Override
    public void onFailure(Call<StationsModel> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        Log.d(this.getClass().getSimpleName(), "Internet Fail");
        mView.hideProgress();
    }
}
