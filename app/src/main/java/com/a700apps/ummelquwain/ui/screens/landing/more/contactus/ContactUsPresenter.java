package com.a700apps.ummelquwain.ui.screens.landing.more.contactus;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsModel;
import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsResultModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 8/27/2017.
 */

public class ContactUsPresenter implements ContactUsContract.UserAction, LifecycleObserver {

    private ContactUsContract.View mView;
    private Context mContext;
    private ContactUsResultModel mModel;
    private Call<ContactUsModel> mGetContactUsCall;
    private Realm mRealm;

    public ContactUsPresenter(Context context, ContactUsContract.View view, Lifecycle lifecycle) {
        mView = view;
        mContext = context;
        lifecycle.addObserver(this);
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        ContactUsResultModel query = mRealm.where(ContactUsResultModel.class).findFirst();
        if (query != null){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mGetContactUsCall = MyApplication.get(mContext).getApiService().getContactUs(new LanguageRequestModel(1));
        mGetContactUsCall.enqueue(new Callback<ContactUsModel>() {
            @Override
            public void onResponse(@NonNull Call<ContactUsModel> call, @NonNull Response<ContactUsModel> response) {
                Log.i("response", response.body().getResult().getFacebookUrl());
                mModel = response.body().getResult();
                mView.hideProgress();

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mModel);
                mRealm.commitTransaction();

                mView.updateUI(mModel);
            }

            @Override
            public void onFailure(@NonNull Call<ContactUsModel> call, @NonNull Throwable t) {
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

    @Override
    public void showFacebook() {
        if (mModel != null) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getFacebookUrl()));
            mContext.startActivity(in);
        }
    }

    @Override
    public void showTwitter() {
        if (mModel != null) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getTwitterUrl()));
            mContext.startActivity(in);
        }
    }

    @Override
    public void showInstagram() {
        if (mModel != null) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getInstagramUrl()));
            mContext.startActivity(in);
        }
    }

    @Override
    public void showLinkedIn() {
        if (mModel != null) {
//        Intent in = new  Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getWebsiteLink()));
//        mContext.startActivity(in);
        }
    }

    @Override
    public void openMap() {
        if (mModel != null) {
            String uri = "geo:" + mModel.getLatitude() + mModel.getLatitude();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mContext.startActivity(intent);
        }
    }
}
