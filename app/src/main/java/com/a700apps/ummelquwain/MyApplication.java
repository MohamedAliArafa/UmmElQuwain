package com.a700apps.ummelquwain;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import com.a700apps.ummelquwain.dagger.Application.component.ApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.component.DaggerApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.module.ContextModule;
import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.player.PlayerCallback;
import com.a700apps.ummelquwain.player.PlayerService;
import com.a700apps.ummelquwain.service.ApiService;
import com.a700apps.ummelquwain.utilities.Constants;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;

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
    private String mDeviceID;
    private static boolean isServiceStarted = false;
    private static int mCurrentStationID;

    private PlayerService mPlayerService;
    private ServiceConnection mServiceConnection;
    private Intent mServiceIntent;

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

        mServiceIntent = new Intent(this, PlayerService.class);
        mServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
    }

    public void startService(StationResultModel model, PlayerCallback callback) {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    PlayerService.ServiceBinder binder = (PlayerService.ServiceBinder) iBinder;
                    mPlayerService = binder.getService();
                    mPlayerService.preparePlayer(model, callback);
                    isServiceStarted = true;
                    mCurrentStationID = model.getStationID();
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceStarted = false;
                }
            };
        } else {
            if (mPlayerService != null) {
//                if (mCurrentStationID == model.getStationID())
                if (!mPlayerService.isPreparing)
                    mPlayerService.togglePlay(callback);
//                else
//                    mPlayerService.preparePlayer(model, callback);
            }
        }
        bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopPlayer() {
        unBindService();
    }

    private void unBindService() {
        if (isServiceStarted) {
            unbindService(mServiceConnection);
            isServiceStarted = false;
        }
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
        Log.d("LoginID:", deviceId);
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }
}
