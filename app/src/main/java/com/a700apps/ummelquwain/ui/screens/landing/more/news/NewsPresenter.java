package com.a700apps.ummelquwain.ui.screens.landing.more.news;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.news.details.NewsDetailsFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsPresenter implements NewsContract.UserAction, LifecycleObserver {

    private Context mContext;
    private NewsContract.ModelView mView;
    private FragmentManager mFragmentManager;
    private Call<NewsBarModel> mNewsCall;
    private List<NewsBarResultModel> mModel;

    public NewsPresenter(Context context, NewsContract.ModelView view, FragmentManager fragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        mContext = context;
        mView = view;
        mFragmentManager = fragmentManager;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mNewsCall = MyApplication.get(mContext).getApiService()
                .getAllNews(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mNewsCall.enqueue(new Callback<NewsBarModel>() {
            @Override
            public void onResponse(@NonNull Call<NewsBarModel> call, @NonNull Response<NewsBarModel> response) {
                try {
                    mModel = response.body().getResult();
                    mView.updateUI(mModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<NewsBarModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int newsID) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, NewsDetailsFragment.newInstance(newsID)).commit();
    }
}
