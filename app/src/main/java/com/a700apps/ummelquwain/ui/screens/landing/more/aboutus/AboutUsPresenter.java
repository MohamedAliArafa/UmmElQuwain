package com.a700apps.ummelquwain.ui.screens.landing.more.aboutus;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class AboutUsPresenter implements AboutUsContract.UserAction, LifecycleObserver {

    private AboutUsContract.View mView;
    private AboutUsResultModel mModel;
    private Context mContext;
    Call<AboutUsModel> mGetContactUsCall;

    public AboutUsPresenter(AboutUsContract.View view, Context context, Lifecycle lifecycle) {
        mView = view;
        mContext = context;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();
        mGetContactUsCall = MyApplication.get(mContext).getApiService().getAboutUs(new LanguageRequestModel(1));
        mGetContactUsCall.enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(@NonNull Call<AboutUsModel> call, @NonNull Response<AboutUsModel> response) {
                Log.i("response", response.body().getResult().getMangerName());
                mModel = response.body().getResult();
                mView.hideProgress();
                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<AboutUsModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
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
