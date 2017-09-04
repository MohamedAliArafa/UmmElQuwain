package com.a700apps.ummelquwain.ui.screens.splash;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;

public class SplashFragment extends Fragment implements SplashContract.View, LifecycleRegistryOwner {

    private static final long SPLASH_DISPLAY_LENGTH = 5000;
    SplashPresenter splashPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);


    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        splashPresenter = new SplashPresenter(this, getLifecycle());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    Runnable mRunnable;
    Handler mHandler;

    @Override
    public void startTimer() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new LandingFragment(), "landing");
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.commit();
            }
        };
        mHandler = new Handler();

        mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void killTimer() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
