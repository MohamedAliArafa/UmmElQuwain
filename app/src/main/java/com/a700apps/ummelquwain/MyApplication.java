package com.a700apps.ummelquwain;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.a700apps.ummelquwain.dagger.Application.component.ApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.component.DaggerApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.module.ContextModule;
import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/*
 * Created by mohamed.arafa on 8/27/2017.
 */

public class MyApplication extends Application {

    private ApiService mApiService;
    private Picasso mPicasso;
    private Realm mRealm;
    LanguageRequestModel mLanguageRequestModel;
    Locale mEnLocale;
    Locale mArLocale;
    Configuration mConfiguration;
    DisplayMetrics mDisplayMetrics;
    Resources mResources;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        mResources = getResources();
        mConfiguration = mResources.getConfiguration();
        mDisplayMetrics = mResources.getDisplayMetrics();

        mEnLocale = new Locale("en");
        mArLocale = new Locale("ar");

        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        mRealm = Realm.getDefaultInstance();

        mLanguageRequestModel = mRealm.where(LanguageRequestModel.class).findFirst();
        if (mLanguageRequestModel == null) {
            mLanguageRequestModel = new LanguageRequestModel(2);
            mLanguageRequestModel.setId(1);
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(mLanguageRequestModel);
            mRealm.commitTransaction();
        } else {
            setLocale(getLanguage());
        }

        mPicasso = applicationComponent.getPicasso();
        mApiService = applicationComponent.getService();
    }

    public void setLocale(int lang) {
        if (lang == 1) {
            Locale.setDefault(mArLocale);
            mConfiguration.setLocale(mArLocale);
            mResources.updateConfiguration(mConfiguration, mDisplayMetrics);
        } else {
            Locale.setDefault(mEnLocale);
            mConfiguration.setLocale(mEnLocale);
            mResources.updateConfiguration(mConfiguration, mDisplayMetrics);
        }
        getApplicationContext().createConfigurationContext(mConfiguration);
    }

    public ApiService getApiService() {
        return mApiService;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public int getLanguage() {
        return mLanguageRequestModel.getLanguage();
    }

    public String getUser() {
        User userID = mRealm.where(User.class).findFirst();
        return userID == null ? "-1" : userID.getID();
    }

    public void toggleLanguage() {
        switch (mLanguageRequestModel.getLanguage()) {
            case 1:
                setLocale(2);
                mRealm.beginTransaction();
                mLanguageRequestModel.setLanguage(2);
                mRealm.commitTransaction();
                break;
            case 2:
                setLocale(1);
                mRealm.beginTransaction();
                mLanguageRequestModel.setLanguage(1);
                mRealm.commitTransaction();
                break;
        }
    }
}
