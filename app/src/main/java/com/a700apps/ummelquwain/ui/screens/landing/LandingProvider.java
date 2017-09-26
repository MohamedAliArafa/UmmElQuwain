package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationContract;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class LandingProvider implements LifecycleObserver, LandingContract.UserAction {

    private final Player mPlayer;
    private final Context mContext;
    private LandingFragment mView;
    private Lifecycle mLifeCycle;
    private RealmResults<StationResultModel> mStationModel;
    private RealmResults<ProgramResultModel> mProgramModel;
    private Realm mRealm;
    private int favStatus;
    private Call<MessageModel> mFavCall;
    private boolean playing;
    public LandingProvider(LandingFragment view, Context context, Lifecycle lifecycle) {
        mView = view;
        mLifeCycle = lifecycle;
        mContext = context;
        lifecycle.addObserver(this);
        mPlayer = MyApplication.get(context).getPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void init() {
        mRealm = Realm.getDefaultInstance();
        mStationModel = mRealm.where(StationResultModel.class).findAll();
        playing = false;
        mStationModel.addChangeListener(stationResultModels -> {
            for (StationResultModel station : mStationModel) {
                if (station.isPlaying()) {
                    mView.showPlayer(station);
                    playing = true;
                }
            }
        });

        mProgramModel = mRealm.where(ProgramResultModel.class).findAll();
        mProgramModel.addChangeListener(stationResultModels -> {
            for (ProgramResultModel program : mProgramModel) {
                if (program.isPlaying()) {
                    mView.showPlayer(program);
                    playing = true;
                }
            }
        });

        if (!playing)
            mView.hidePlayer();

        mView.setupViewPager();
        mView.setupTabLayout();
    }

    @Override
    public void playStream(StationResultModel model) {
        mPlayer.playStream(model);
    }

    @Override
    public void playStream(ProgramResultModel model) {
        mPlayer.playStream(model);
    }

    @Override
    public void setFav(int itemID, int isFav, StationContract.adapterCallback callback) {
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        favStatus = isFav;
        if (user.equals("-1"))
            user = null;
        if (deviceId == null) {
            ((MainActivity) mView.getActivity()).requestReadPermission();
            return;
        }
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
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.favCallback(favStatus);
            }
        });

    }
}
