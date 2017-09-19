package com.a700apps.ummelquwain.ui.screens.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;
import com.a700apps.ummelquwain.ui.screens.splash.SplashFragment;
import com.crashlytics.android.Crashlytics;

import java.util.UUID;

import io.fabric.sdk.android.Fabric;

import static com.a700apps.ummelquwain.utilities.Constants.REQUEST_READ_CALENDER_PERMISSION;
import static com.a700apps.ummelquwain.utilities.Constants.REQUEST_READ_PHONE_PERMISSION;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private LandingFragment mContent;
//    private static final int REQUEST_READ_PERMISSION = 123;
    private GestureDetector mGesture;

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
        requestReadPermission();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mContent = (LandingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        try {
            getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("landing");
            if (fragment instanceof LandingFragment) {
                if (((LandingFragment) getSupportFragmentManager().findFragmentByTag("landing")).moveToHome()) {
                    super.onBackPressed();
                }
            } else
                super.onBackPressed();
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
    public void launchLogin() {
        mContent = new LandingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mContent, "landing");
        fragmentTransaction.add(R.id.fragment_container, new LoginFragment(), "login_fragment");
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void requestReadPermission() {
        if (CheckPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            registerDeviceID();
        } else {
            RequestPermission(this, Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PHONE_PERMISSION);
        }
    }

    private void registerDeviceID() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei_id = telephonyManager.getDeviceId();
        String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DeviceID", device_id);
        UUID deviceUuid = new UUID(device_id.hashCode(), ((long) imei_id.hashCode() << 32));
        MyApplication.get(this).setDeviceId(deviceUuid.toString());
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity, Permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{Permission}, Code);
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case REQUEST_READ_PHONE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerDeviceID();
                } else {
                    Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case REQUEST_READ_CALENDER_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    registerDeviceID();
                } else {
                    Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("login_fragment");
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }
}
