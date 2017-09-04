package com.a700apps.ummelquwain;

import android.app.Application;
import android.content.Context;

import com.a700apps.ummelquwain.dagger.Application.component.ApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.component.DaggerApplicationComponent;
import com.a700apps.ummelquwain.dagger.Application.module.ContextModule;
import com.a700apps.ummelquwain.service.ApiService;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class MyApplication extends Application {

    private ApiService mApiService;
    private Picasso mPicasso;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        mPicasso = applicationComponent.getPicasso();
        mApiService = applicationComponent.getService();
    }

    public ApiService getApiService(){
        return mApiService;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }
}
