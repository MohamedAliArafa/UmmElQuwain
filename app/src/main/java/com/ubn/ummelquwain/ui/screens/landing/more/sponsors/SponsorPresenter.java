package com.ubn.ummelquwain.ui.screens.landing.more.sponsors;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.request.LanguageRequestModel;
import com.ubn.ummelquwain.models.response.Sponsors.SponsorModel;
import com.ubn.ummelquwain.models.response.Sponsors.SponsorResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class SponsorPresenter implements SponsorsContract.UserAction, LifecycleObserver {

    private SponsorsContract.ModelView mView;
    private Context mContext;
    private List<SponsorResultModel> mModel;
    private Call<SponsorModel> mGetSponsorCall;

    public SponsorPresenter(Context context, SponsorsContract.ModelView view, Lifecycle lifecycle) {
        mContext = context;
        mView = view;
        lifecycle.addObserver(this);
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        Log.i("Status","GetData Init");
        mView.showProgress();
        mGetSponsorCall = MyApplication.get(mContext).getApiService()
                .getAllSponsors(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mGetSponsorCall.enqueue(new Callback<SponsorModel>() {
            @Override
            public void onResponse(@NonNull Call<SponsorModel> call, @NonNull Response<SponsorModel> response) {
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
            public void onFailure(@NonNull Call<SponsorModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");

                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){
        if (mGetSponsorCall != null){
            mGetSponsorCall.cancel();
        }
    }
}
