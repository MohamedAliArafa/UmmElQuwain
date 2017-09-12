package com.a700apps.ummelquwain.ui.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;
import com.a700apps.ummelquwain.ui.screens.splash.SplashFragment;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private LandingFragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction.replace(R.id.fragment_container, SplashFragment.newInstance());
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            fragmentTransaction.commit();
        } else {
            mContent = (LandingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!= null)
            mContent = (LandingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("landing");
            if (fragment instanceof LandingFragment)
                if (((LandingFragment) getSupportFragmentManager().findFragmentByTag("landing")).moveToHome()) {
                    super.onBackPressed();
                }
        }
    }

    @Override
    public void launchLanding() {
        mContent = new LandingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mContent, "landing");
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("login_fragment");
        fragment.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
