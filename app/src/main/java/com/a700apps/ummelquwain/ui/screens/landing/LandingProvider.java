package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.CommentRequestModel;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationContract;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

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
    private Call<MessageModel> mCommentCall;
    private boolean playing;

    public LandingProvider(LandingFragment view, Context context, Lifecycle lifecycle) {
        mView = view;
        mLifeCycle = lifecycle;
        mContext = context;
        lifecycle.addObserver(this);
        mPlayer = MyApplication.get(context).getPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
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

    @Override
    public void shareFromPlayer(StationResultModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getStationName()
                + "\n" + model.getStationFrequency() + " - " + model.getStationLanguage()
                + "\n" + model.getStationWebsite());
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.share_with)));
    }

    @Override
    public void shareFromPlayer(ProgramResultModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getProgramName()
                + "\n" + model.getBroadcasterName()
                + "\n" + model.getProgramDescription());
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.share_with)));
    }

    @Override
    public void addComment(ProgramResultModel model) {
        String user = MyApplication.get(mContext).getUser();
        if (user.equals("-1")) {
            Toast.makeText(mContext, R.string.toast_must_login, Toast.LENGTH_SHORT).show();
            ((MainActivity) mView.getActivity()).launchLogin();
            return;
        }
        new LovelyTextInputDialog(mContext, R.style.AppTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(model.getProgramName())
                .setMessage(R.string.enter_your_comment)
                .setConfirmButton(android.R.string.ok, text -> {
                    if (text.isEmpty()) {
                        Toast.makeText(mContext, R.string.toast_empty_comment, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCommentCall = MyApplication.get(mContext).getApiService()
                            .addComment(new CommentRequestModel(user, model.getProgramID(), text));
                    mCommentCall.enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                            try {
                                Toast.makeText(mContext, R.string.toast_thanks_for_comment, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(mContext, R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }
}
