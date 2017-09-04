package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class LandingProvider implements LifecycleObserver{

    LandingContract.View mView;
    Lifecycle mLifeCycle;

    public LandingProvider(LandingContract.View view, Lifecycle lifecycle) {
        mView = view;
        mLifeCycle = lifecycle;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void init(){
        mView.setupViewPager();
        mView.setupTabLayout();
    }
}
