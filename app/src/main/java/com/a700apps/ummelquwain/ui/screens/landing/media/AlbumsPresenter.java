package com.a700apps.ummelquwain.ui.screens.landing.media;

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
import com.a700apps.ummelquwain.models.response.Albums.AlbumResultModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumsModel;
import com.a700apps.ummelquwain.ui.screens.landing.media.albums.AlbumFragment;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class AlbumsPresenter implements AlbumsContract.UserAction, LifecycleObserver {

    private Context mContext;
    private AlbumsContract.ModelView mView;
    private FragmentManager mFragmentManager;
    private Call<AlbumsModel> mAlbumsCall;
    private List<AlbumResultModel> mModel;
    private Realm mRealm;

    public AlbumsPresenter(Context mContext, AlbumsContract.ModelView mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<AlbumResultModel> query = mRealm.where(AlbumResultModel.class).findAll();
        if (query.isLoaded() && !query.isEmpty()) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mAlbumsCall = MyApplication.get(mContext).getApiService()
                .getAllAlbums(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mAlbumsCall.enqueue(new Callback<AlbumsModel>() {
            @Override
            public void onResponse(@NonNull Call<AlbumsModel> call, @NonNull Response<AlbumsModel> response) {
                try {
                    mModel = response.body().getResult();

                    mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(mModel));
//                    mRealm.copyToRealmOrUpdate(mModel);
//                    mRealm.commitTransaction();

                    mView.updateUI(mModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<AlbumsModel> call, @NonNull Throwable t) {

                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int albumID, String albumDesc) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, AlbumFragment.newInstance(albumID, albumDesc)).commit();
    }

    @Override
    public void search(String keyword) {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<AlbumResultModel> query = mRealm.where(AlbumResultModel.class)
                .contains("albumName", keyword, Case.INSENSITIVE)
                .findAll();
        if (query.isLoaded()) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
    }
}
