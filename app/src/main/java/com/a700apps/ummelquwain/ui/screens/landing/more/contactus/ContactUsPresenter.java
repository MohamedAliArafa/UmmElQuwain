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
import com.a700apps.ummelquwain.R;
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

    private ContactUsContract.ModelView mView;
    private Context mContext;
    private ContactUsResultModel mModel;
    private Call<ContactUsModel> mGetContactUsCall;
    private Realm mRealm;

    public ContactUsPresenter(Context context, ContactUsContract.ModelView view, Lifecycle lifecycle) {
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
        if (query != null) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mGetContactUsCall = MyApplication.get(mContext).getApiService()
                .getContactUs(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mGetContactUsCall.enqueue(new Callback<ContactUsModel>() {
            @Override
            public void onResponse(@NonNull Call<ContactUsModel> call, @NonNull Response<ContactUsModel> response) {
                try {
                    Log.i("response", response.body().getResult().getFacebookUrl());
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
            public void onFailure(@NonNull Call<ContactUsModel> call, @NonNull Throwable t) {

                t.printStackTrace();

                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");

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
        try {
            if (mModel != null && mModel.getFacebookUrl() != null) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getFacebookUrl()));
                mContext.startActivity(in);
            } else {
                Toast.makeText(mContext.getApplicationContext(), R.string.error_open, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showTwitter() {
        try {
            if (mModel != null && mModel.getTwitterUrl() != null) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getTwitterUrl()));
                mContext.startActivity(in);
            } else {
                Toast.makeText(mContext.getApplicationContext(), R.string.error_open, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInstagram() {
        try {
            if (mModel != null && mModel.getInstagramUrl() != null) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getInstagramUrl()));
                mContext.startActivity(in);
            } else {
                Toast.makeText(mContext.getApplicationContext(), R.string.error_open, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLinkedIn() {
        try {
            if (mModel != null && mModel.getLinkedInUrl() != null) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(mModel.getLinkedInUrl()));
                mContext.startActivity(in);
            } else {
                Toast.makeText(mContext.getApplicationContext(), R.string.error_open, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openMap() {
        try {
            if (mModel != null) {
                String uri = "geo:" + mModel.getLatitude() + "," + mModel.getLongtiude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
