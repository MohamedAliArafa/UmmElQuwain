package com.a700apps.ummelquwain.ui.screens.landing.programs.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.ProgramDetailsRequestModel;
import com.a700apps.ummelquwain.models.response.program.ProgramModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.utilities.Utility;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramPresenter implements ProgramContract.UserAction, LifecycleObserver {
    private Lifecycle mLifecycle;
    private Player mPlayer;
    private Context mContext;
    private ProgramContract.ModelView mView;
    private Call<ProgramModel> mProgramCall;
    private ProgramResultModel mModel;
    private Realm mRealm;
    private int mProgramID;

    public ProgramPresenter(Context mContext, int mProgramID, ProgramContract.ModelView mView, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mLifecycle = lifecycle;
        this.mView = mView;
        this.mProgramID = mProgramID;
        mPlayer = MyApplication.get(mContext).getPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        mModel.removeAllChangeListeners();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        ProgramResultModel query = mRealm.where(ProgramResultModel.class).equalTo("programID", mProgramID).findFirst();
        if (query != null) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
            mView.setupViewPager();
            mView.setupTabLayout();
        }
        mProgramCall = MyApplication.get(mContext).getApiService()
                .getProgramDetails(
                        new ProgramDetailsRequestModel(MyApplication.get(mContext).getLanguage(),
                                MyApplication.get(mContext).getUser(), mProgramID));
        mProgramCall.enqueue(new Callback<ProgramModel>() {
            @Override
            public void onResponse(@NonNull Call<ProgramModel> call, @NonNull Response<ProgramModel> response) {
                try {
                    Utility.addProgramToRealm(response.body().getResult(),
                            () -> mModel.addChangeListener(stationResultModels -> {
                                mView.updateUI(mModel);
                            }));
                    if (!mLifecycle.getCurrentState().isAtLeast(Lifecycle.State.DESTROYED))
                        mView.updateUI(mModel);
                    mView.setupViewPager();
                    mView.setupTabLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ProgramModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });

        mModel.addChangeListener(stationResultModels -> {
//            if (!mLifeCycle.getCurrentState().isAtLeast(Lifecycle.State.DESTROYED))
            mView.updateUI(mModel);
        });
    }

    @Override
    public void playStream() {
        mPlayer.playStream(mModel);
    }
}
