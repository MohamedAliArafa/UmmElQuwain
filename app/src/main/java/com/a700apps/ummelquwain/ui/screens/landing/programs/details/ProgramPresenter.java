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

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramPresenter implements ProgramContract.UserAction, LifecycleObserver {
    private Context mContext;
    private ProgramContract.View mView;
    private Call<ProgramModel> mProgramCall;
    private ProgramResultModel mModel;
    private Realm mRealm;
    private int mProgramID;

    public ProgramPresenter(Context mContext, int mProgramID, ProgramContract.View mView, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mProgramID = mProgramID;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        ProgramResultModel query = mRealm.where(ProgramResultModel.class).equalTo("stationID", mProgramID).findFirst();
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
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();

                mView.updateUI(mModel);

                mView.setupViewPager();
                mView.setupTabLayout();

            }

            @Override
            public void onFailure(@NonNull Call<ProgramModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
