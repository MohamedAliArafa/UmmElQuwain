package com.a700apps.ummelquwain.ui.screens.splash;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

class SplashPresenter implements LifecycleObserver {

    private SplashContract.View view;

    SplashPresenter(SplashContract.View view, Lifecycle lifecycle) {
        this.view = view;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        view.startTimer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void stop() {
        view.killTimer();
    }
}
