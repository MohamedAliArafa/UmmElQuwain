package com.ubn.ummelquwain.ui.screens.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.ui.screens.main.MainActivity;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class SplashFragment extends Fragment implements SplashContract.ModelView {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    SplashPresenter splashPresenter;


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
        mRunnable = () -> {
            /* Create an Intent that will start the Menu-Activity. */
            if (!((MyApplication) getContext().getApplicationContext()).getUser().equals("-1"))
                ((MainActivity) getActivity()).launchLanding();
            else
                ((MainActivity) getActivity()).launchLogin();
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
}
