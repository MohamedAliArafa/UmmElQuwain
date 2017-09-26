package com.a700apps.ummelquwain.ui.screens.landing.more.aboutus;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsResultModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 8/28/2017.
 */

public class AboutUsPresenter implements AboutUsContract.UserAction, LifecycleObserver {

    private AboutUsContract.ModelView mView;
    private AboutUsResultModel mModel;
    private Context mContext;
    Call<AboutUsModel> mGetContactUsCall;
    private Realm mRealm;

    public AboutUsPresenter(AboutUsContract.ModelView view, Context context, Lifecycle lifecycle) {
        mView = view;
        mContext = context;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        AboutUsResultModel query = mRealm.where(AboutUsResultModel.class).findFirst();
        if (query != null){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mGetContactUsCall = MyApplication.get(mContext).getApiService()
                .getAboutUs(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mGetContactUsCall.enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(@NonNull Call<AboutUsModel> call, @NonNull Response<AboutUsModel> response) {
                try {
                    Log.i("response", response.body().getResult().getMangerName());
                    mModel = response.body().getResult();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(mModel);
                    mRealm.commitTransaction();
                    mView.updateUI(mModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<AboutUsModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

                mView.hideProgress();
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mGetContactUsCall != null) {
            mGetContactUsCall.cancel();
        }
    }
}
