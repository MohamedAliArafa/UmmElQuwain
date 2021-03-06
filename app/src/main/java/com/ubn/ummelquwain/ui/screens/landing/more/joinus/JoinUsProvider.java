package com.ubn.ummelquwain.ui.screens.landing.more.joinus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.request.JoinUsRequestModel;
import com.ubn.ummelquwain.models.response.Message.MessageModel;
import com.ubn.ummelquwain.service.ProgressRequestBody;
import com.ubn.ummelquwain.utilities.Utility;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by mohamed.arafa on 8/30/2017.
 */

public class JoinUsProvider implements JoinUsContract.UserAction, ProgressRequestBody.UploadCallbacks {

    private final JoinUsFragment mView;
    private final FragmentManager mFragmentManager;
    private Context mContext;
    private Call<MessageModel> mJoinUsCall;
    private MessageModel mModel;
    private String mResponse;

    public JoinUsProvider(Context mContext, JoinUsFragment mView, FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void uploadFile(Intent data, JoinUsContract.fileCallback callback) {
        mView.showProgress();
        Uri selectedFile = data.getData();
        String filePath = Utility.getRealPathFromURIPath(selectedFile, mView.getActivity());
        File file = new File(filePath);
        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<MessageModel> responseBodyCall = MyApplication
                .get(mContext).getApiService().addRecord(multipartBody);
        responseBodyCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                try {
                    Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());
                    try {
                        mView.hideProgress();
                        Log.d("Success", "body " + response.body().getResult().getMessage());
                        mResponse = response.body().getResult().getMessage();
                        callback.onFileUploaded(mResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    Toast.makeText(mContext, R.string.toast_thanks_for_your_contribution, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onProgressUpdate(int percentage) {
        mView.setProgress(percentage);
    }

    @Override
    public void onError() {
        mView.hideProgress();
        Toast.makeText(mContext, "Upload Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        mView.hideProgress();
        Toast.makeText(mContext, "Upload Finished", Toast.LENGTH_SHORT).show();
    }
}
