package com.ubn.ummelquwain.ui.screens.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.User;
import com.ubn.ummelquwain.models.response.Message.MessageModel;
import com.ubn.ummelquwain.ui.screens.landing.LandingFragment;
import com.ubn.ummelquwain.ui.screens.main.MainActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ubn.ummelquwain.utilities.Constants.LANDING_FRAGMENT_KEY;

/*
 * Created by mohamed.arafa on 9/11/2017.
 */

public class LoginPresenter implements LoginContract.UserAction {
    private final FragmentManager mFragmentManger;
    private Context mContext;
    private MainActivity activity;
    private Call<MessageModel> mLoginCall;
    private User mModel;
    private Realm mRealm;

    public LoginPresenter(Context mContext, FragmentManager mFragmentManger,MainActivity activity) {
        this.mContext = mContext;
        this.mFragmentManger = mFragmentManger;
        this.activity = activity;
    }

    @Override
    public void socialLogin(User user) {
        mModel = user;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.where(User.class).findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
        mLoginCall = MyApplication.get(mContext).getApiService().loginUser(user);
        mLoginCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                try {
                    mModel.setID(response.body().getResult().getMessage());
                    Log.d("LoginID:", mModel.getID());
                    if (mModel.getDeviceID() != null)
                        Log.d("LoginDeviceID:", mModel.getDeviceID());
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(mModel);
                    mRealm.commitTransaction();
                    Toast.makeText(mContext, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                    if (mFragmentManger.getBackStackEntryCount() == 0)
                        activity.launchLanding();
                    else {
                        LandingFragment fragment = (LandingFragment) mFragmentManger.findFragmentByTag(LANDING_FRAGMENT_KEY);
                        if (fragment == null) { //fragment not in back stack, create it.
                            FragmentTransaction ft = mFragmentManger.beginTransaction();
                            ft.replace(R.id.fragment_container, LandingFragment.newInstance());
                            ft.addToBackStack(LANDING_FRAGMENT_KEY);
                            ft.commit();
                        } else {
                            try {
                                mFragmentManger.popBackStack();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            fragment.moveToHome();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
