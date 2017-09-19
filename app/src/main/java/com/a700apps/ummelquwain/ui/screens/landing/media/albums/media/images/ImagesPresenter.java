package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.images;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.AlbumContentRequestModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumModel;
import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.MediaContract;
import com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.images.details.ImageDetailsFragment;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 9/4/2017.
 */

public class ImagesPresenter implements MediaContract.UserAction, LifecycleObserver {

    private Context mContext;
    private MediaContract.View mView;
    private FragmentManager mFragmentManager;
    private Call<AlbumModel> mAlbumsCall;
    private List<MediaResultModel> mModel;
    private Integer albumID;
    private Integer mediaType;
    private Realm mRealm;

    public ImagesPresenter(Context mContext, MediaContract.View mView, FragmentManager mFragmentManager, Lifecycle lifecycle, int albumID, int mediaType) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        this.albumID = albumID;
        this.mediaType = mediaType;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<MediaResultModel> query = mRealm.where(MediaResultModel.class)
                .equalTo("albumID", albumID).equalTo("mediaType", 1).findAll();
        if (query.isLoaded() && !query.isEmpty()){
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mAlbumsCall = MyApplication.get(mContext).getApiService()
                .getAlbumContent(new AlbumContentRequestModel(
                        MyApplication.get(mContext).getLanguage(), albumID, mediaType));
        mAlbumsCall.enqueue(new Callback<AlbumModel>() {
            @Override
            public void onResponse(@NonNull Call<AlbumModel> call, @NonNull Response<AlbumModel> response) {
                try {
                    mModel = response.body().getResult().getLstAlbumContent();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(mModel);
                    mRealm.commitTransaction();
                    mView.updateUI(mModel);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<AlbumModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(List<MediaResultModel> media, int position) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, ImageDetailsFragment.newInstance(media, position)).commit();
    }
}
