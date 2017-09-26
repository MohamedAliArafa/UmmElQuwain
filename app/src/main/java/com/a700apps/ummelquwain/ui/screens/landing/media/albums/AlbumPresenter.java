package com.a700apps.ummelquwain.ui.screens.landing.media.albums;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class AlbumPresenter implements AlbumContract.UserActions, LifecycleObserver {
    AlbumContract.ModelView mView;
    Lifecycle mLifeCycle;

    public AlbumPresenter(AlbumContract.ModelView view, Lifecycle lifecycle) {
        mView = view;
        mLifeCycle = lifecycle;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void init() {
        mView.setupViewPager();
        mView.setupTabLayout();
    }
}
