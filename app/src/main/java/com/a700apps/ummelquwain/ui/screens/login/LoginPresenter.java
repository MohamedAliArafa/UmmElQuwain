package com.a700apps.ummelquwain.ui.screens.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 9/11/2017.
 */

public class LoginPresenter implements LoginContract.UserAction {
    private Context mContext;
    private Call<MessageModel> mLoginCall;
    private User mModel;
    private Realm mRealm;

    public LoginPresenter(Context mContext) {
        this.mContext = mContext;
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
