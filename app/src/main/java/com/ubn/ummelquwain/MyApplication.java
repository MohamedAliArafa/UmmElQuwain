package com.ubn.ummelquwain;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.ubn.ummelquwain.dagger.Application.component.ApplicationComponent;
import com.ubn.ummelquwain.dagger.Application.component.DaggerApplicationComponent;
import com.ubn.ummelquwain.dagger.Application.module.ContextModule;
import com.ubn.ummelquwain.models.User;
import com.ubn.ummelquwain.models.request.LanguageRequestModel;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.player.Player;
import com.ubn.ummelquwain.service.ApiService;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/*
 * Created by mohamed.arafa on 8/27/2017.
 */

public class MyApplication extends Application {

    LanguageRequestModel mLanguageRequestModel;
    Locale mEnLocale;
    Locale mArLocale;
    Configuration mConfiguration;
    DisplayMetrics mDisplayMetrics;
    Resources mResources;
    private ApiService mApiService;
    private Picasso mPicasso;
    private Realm mRealm;
    private String mDeviceID;

    private Player mPlayer;

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

        Twitter.initialize(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        mRealm = Realm.getDefaultInstance();
//        new RealmBrowser.Builder(this)
//                // add class, you want to view
//                .add(mRealm, StationResultModel.class)
//                .add(mRealm, ProgramResultModel.class)
//                // call method showNotification()
//                .showNotification();

        mPlayer = MyApplication.get(this).getPlayer();

        mLanguageRequestModel = mRealm.where(LanguageRequestModel.class).findFirst();
        if (mLanguageRequestModel == null) {
            mLanguageRequestModel = new LanguageRequestModel(2);
            mLanguageRequestModel.setId(1);
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(mLanguageRequestModel);
            mRealm.commitTransaction();
            mRealm.executeTransaction(realm -> {
                realm.delete(StationResultModel.class);
                realm.delete(ProgramResultModel.class);
            });
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
        onConfigurationChanged(mConfiguration);
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

    public Player getPlayer() {
        if (mPlayer == null)
            return new Player(this);
        else
            return mPlayer;
    }

    public int getLanguage() {
        return mLanguageRequestModel.getLanguage();
    }

    public String getUser() {
        User userID = mRealm.where(User.class).findFirst();
        return userID == null ? "-1" : userID.getID();
    }

    public String getDeviceID() {
        return mDeviceID;
    }

    public void setDeviceId(String deviceId) {
        mDeviceID = deviceId;
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
//        mRealm.executeTransaction(realm -> {
//            realm.delete(StationResultModel.class);
//            realm.delete(ProgramResultModel.class);
//        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }
}
