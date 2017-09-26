package com.a700apps.ummelquwain.ui.screens.landing.more.news.details;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.NewsDetailsRequestModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarDetailModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsDetailPresenter implements NewsDetailContract.UserAction, LifecycleObserver {

    private NewsDetailContract.ModelView mView;
    private Context mContext;
    private NewsBarResultModel mModel;
    private Call<NewsBarDetailModel> mGetNewsDetails;
    private int mNewsID;

    public NewsDetailPresenter(NewsDetailContract.ModelView view, Context context, Lifecycle lifecycle, int newsID) {
        lifecycle.addObserver(this);
        mView =view;
        mContext = context;
        mNewsID=newsID;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        Log.i("Status","GetData Init");
        mView.showProgress();
        mGetNewsDetails = MyApplication.get(mContext).getApiService()
                .getNewsDetails(new NewsDetailsRequestModel(
                        MyApplication.get(mContext).getLanguage(), mNewsID));
        mGetNewsDetails.enqueue(new Callback<NewsBarDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<NewsBarDetailModel> call, @NonNull Response<NewsBarDetailModel> response) {
                try {
                    Log.i("response", response.body().getResult().toString());
                    mModel = response.body().getResult();

                    mView.updateUI(mModel);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<NewsBarDetailModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void shareNews(NewsBarResultModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getNewsTitle()
                + "\n" + model.getNewsTime() + " - " +model.getNewsDate()
                + "\n" + model.getNewsDescription());
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.share_with)));
    }
}
