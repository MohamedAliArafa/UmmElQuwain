package com.a700apps.ummelquwain.ui.screens.landing.programs;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.SearchRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramsModel;
import com.a700apps.ummelquwain.ui.screens.landing.programs.details.ProgramFragment;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramsPresenter implements ProgramsContract.UserAction, LifecycleObserver{
    private Context mContext;
    private ProgramsContract.View mView;
    private FragmentManager mFragmentManager;
    private Call<ProgramsModel> mStationsCall;
    private List<ProgramResultModel> mModel;
    private Realm mRealm;

    public ProgramsPresenter(Context mContext, ProgramsContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
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
        RealmResults<ProgramResultModel> query = mRealm.where(ProgramResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .getAllPrograms(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), MyApplication.get(mContext).getUser()));
        mStationsCall.enqueue(new Callback<ProgramsModel>() {
            @Override
            public void onResponse(@NonNull Call<ProgramsModel> call, @NonNull Response<ProgramsModel> response) {
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();

                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<ProgramsModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int programID) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, ProgramFragment.newInstance(programID)).commit();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void search(String keyword) {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<ProgramResultModel> query = mRealm.where(ProgramResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .searchPrograms(new SearchRequestModel(keyword, MyApplication.get(mContext).getUser(), MyApplication.get(mContext).getLanguage()));
        mStationsCall.enqueue(new Callback<ProgramsModel>() {
            @Override
            public void onResponse(@NonNull Call<ProgramsModel> call, @NonNull Response<ProgramsModel> response) {
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();
                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<ProgramsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
