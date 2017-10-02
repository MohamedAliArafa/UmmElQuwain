package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

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
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.player.StationPlayerService;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsContract;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.a700apps.ummelquwain.utilities.Constants;

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
    private FavFragment mView;
    private FragmentManager mFragmentManager;
    private Call<StationsModel> mStationCall;
    private RealmResults<StationResultModel> mStationModel;

    private Call<SponsorModel> mSponsorCall;
    private List<SponsorResultModel> mSponsorModel;

    private Realm mRealm;
    private Call<MessageModel> mFavCall;
    private int favStatus;

    private ServiceConnection mServiceConnection;
    private Intent mServiceIntent;
    private StationPlayerService mPlayerService;

    private static boolean isServiceStarted = false;
    private Player mPlayer;
    private boolean playing;

    public FavPresenter(Context mContext, FavFragment mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        mServiceIntent = new Intent(mContext, StationPlayerService.class);
        mServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        mPlayer = MyApplication.get(mContext).getPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void resume() {
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> model = mRealm.where(StationResultModel.class).findAll();
        playing = false;
//        getStationData();
//        getSponsorData();
        model.addChangeListener(stationResultModels -> {
//            getStationData();
            for (StationResultModel station : model) {
                if (station.isPlaying()) {
                    ((LandingFragment) mView.getParentFragment()).showPlayer(station);
                    playing = true;
                }
            }
        });
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getStationData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class)
                .equalTo("isFavourite", 1).findAll();
        mStationModel = query;
        if (query.isLoaded()) {
            mView.hideProgress();
            mView.updateFavUI(mStationModel);
        }
        mStationCall = MyApplication.get(mContext).getApiService()
                .getFavStations(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), MyApplication.get(mContext).getUser()));
        mStationCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");
                mView.hideProgress();
            }
        });

        mStationModel.addChangeListener(stationResultModels -> {
            mView.updateFavUI(mStationModel);
        });
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getSponsorData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<SponsorResultModel> query = mRealm.where(SponsorResultModel.class).findAll();
        if (query.isLoaded()) {
            mSponsorModel = query;
            mView.hideProgress();
            mView.updateSponsorUI(mSponsorModel);
        }
        mSponsorCall = MyApplication.get(mContext).getApiService()
                .getAllSponsors(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mSponsorCall.enqueue(new Callback<SponsorModel>() {
            @Override
            public void onResponse(@NonNull Call<SponsorModel> call, @NonNull Response<SponsorModel> response) {
                try {
                    mSponsorModel = response.body().getResult();

                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(mSponsorModel);
                    mRealm.commitTransaction();

                    mView.updateSponsorUI(mSponsorModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();

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
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        favStatus = isFav;
        if (user.equals("-1"))
            user = null;
        if (deviceId == null) {
            ((MainActivity) mView.getActivity()).requestReadPermission();
            return;
        }
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        //Invoke it #onDestroy
        if (isServiceStarted) {
            mContext.unbindService(mServiceConnection);
            isServiceStarted = false;
        }
    }

    @Override
    public void playStream(StationResultModel station) {
        mPlayer.playStream(station);
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
