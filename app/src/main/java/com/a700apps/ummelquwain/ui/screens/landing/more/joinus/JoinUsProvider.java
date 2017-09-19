package com.a700apps.ummelquwain.ui.screens.landing.more.joinus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.models.request.JoinUsRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 8/30/2017.
 */

public class JoinUsProvider implements JoinUsContract.UserAction {

    private final JoinUsContract.View mView;
    private final FragmentManager mFragmentManager;
    private Context mContext;
    private Call<MessageModel> mJoinUsCall;
    private MessageModel mModel;
    String mResponse;

    public JoinUsProvider(Context mContext, JoinUsContract.View mView, FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void uploadFile(Intent data, JoinUsContract.fileCallback callback) {
        Uri selectedFile = data.getData();
        String path = selectedFile.getPath();
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), path);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<MessageModel> responseBodyCall = MyApplication
                .get(mContext).getApiService().addRecord(multipartBody);
        responseBodyCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                try {
                    Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());
                    Log.d("Success", "body " + response.body());
                    mResponse = response.body().getResult().getMessage();
                    callback.onFileUploaded(mResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("failure", "message = " + t.getMessage());
                Log.d("failure", "cause = " + t.getCause());
            }
        });
    }

    @Override
    public void join(JoinUsRequestModel data) {
        mView.showProgress();
        mJoinUsCall = MyApplication.get(mContext).getApiService()
                .joinUs(data);
        mJoinUsCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                mModel = response.body();
                if (mModel.getResult().getMessage().equals("1"))
                    Toast.makeText(mContext, "Thanks for your contribution", Toast.LENGTH_SHORT).show();
                mFragmentManager.popBackStack();
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
