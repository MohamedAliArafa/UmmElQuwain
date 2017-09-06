package com.a700apps.ummelquwain;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.a700apps.ummelquwain.dagger.Application.component.ApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.component.DaggerApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.module.ContextModule;
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

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        Resources resources = getResources();
        mConfiguration = resources.getConfiguration();
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
            mConfiguration.setLocale(mEnLocale);
            getApplicationContext().createConfigurationContext(mConfiguration);
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(mLanguageRequestModel);
            mRealm.commitTransaction();
        }

        mPicasso = applicationComponent.getPicasso();
        mApiService = applicationComponent.getService();
    }

    public ApiService getApiService(){
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

    public void toggleLanguage(){
        switch (mLanguageRequestModel.getLanguage()){
            case 1:
                mRealm.beginTransaction();
                mLanguageRequestModel.setLanguage(2);
                mConfiguration.setLocale(mEnLocale);
                getApplicationContext().createConfigurationContext(mConfiguration);
                mRealm.commitTransaction();
                break;
            case 2:
                mRealm.beginTransaction();
                mLanguageRequestModel.setLanguage(1);
                mConfiguration.setLocale(mArLocale);
                getApplicationContext().createConfigurationContext(mConfiguration);
                mRealm.commitTransaction();
                break;
        }
    }
}
