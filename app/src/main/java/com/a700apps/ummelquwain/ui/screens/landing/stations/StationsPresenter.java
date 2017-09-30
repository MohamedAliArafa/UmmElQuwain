package com.a700apps.ummelquwain.ui.screens.landing.stations;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.request.SearchRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.Message.MessageResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.a700apps.ummelquwain.utilities.Utility;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a700apps.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationsPresenter implements StationsContract.UserAction, LifecycleObserver {
    private Context mContext;
    private StationsFragment mView;
    private FragmentManager mFragmentManager;
    private Call<StationsModel> mStationsCall;
    private Call<MessageModel> mFavCall;
    private RealmResults<StationResultModel> mModel;
    private Realm mRealm;
    private Player mPlayer;
    private boolean playing;

    public StationsPresenter(Context mContext, StationsFragment mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        mPlayer = MyApplication.get(mContext).getPlayer();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class).findAll();
        mModel = query;
        if (query.isLoaded() && !query.isEmpty()) {
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = deviceId;
        mStationsCall = MyApplication.get(mContext).getApiService()
                .getAllStations(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), user));
        mStationsCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
                mView.hideProgress();
                try {
                    Utility.addStationsToRealm(response.body().getResult(),
                            () -> mModel.addChangeListener(stationResultModels -> mView.updateUI(mModel)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int stationID) {
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
                PROGRAM_FRAGMENT_KEY + String.valueOf(stationID), 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, StationFragment.newInstance(stationID));
            ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(stationID));
            ft.commit();
        }
    }

    @Override
    public void playStream(StationResultModel station) {
        mPlayer.playStream(station);
    }

    @Override
    public void search(String keyword) {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<StationResultModel> query = mRealm.where(StationResultModel.class)
                .contains("stationName", keyword, Case.INSENSITIVE)
                .findAll();
        if (query.isLoaded() && query.isEmpty()) {
            query = mRealm.where(StationResultModel.class)
                    .contains("categoryName", keyword, Case.INSENSITIVE)
                    .findAll();
            if (query.isLoaded() && query.isEmpty())
                query = mRealm.where(StationResultModel.class)
                        .contains("stationLanguage", keyword, Case.INSENSITIVE)
                        .findAll();
        }
        if (query.isLoaded()) {
            mView.hideProgress();
            mModel = query;
            mView.updateUI(mModel);
        }
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = deviceId;
        if (deviceId == null) {
            ((MainActivity) mView.getActivity()).requestReadPermission();
            return;
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .searchStations(new SearchRequestModel(keyword, user, MyApplication.get(mContext).getLanguage()));
        mStationsCall.enqueue(new Callback<StationsModel>() {
            @Override
            public void onResponse(@NonNull Call<StationsModel> call, @NonNull Response<StationsModel> response) {
//                mModel = response.body().getResult();
                mView.hideProgress();
                try {
                    try {
                        Utility.addStationsToRealm(response.body().getResult(),
                                () -> mModel.addChangeListener(stationResultModels -> {
                                    mView.updateUI(mModel);
                                }));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StationsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                mView.hideProgress();
            }
        });
    }

    private int favStatus;

    @Override
    public void setFav(int itemID, int isFav, StationsContract.adapterCallback callback) {
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = null;
        if (deviceId == null) {
            ((MainActivity) mView.getActivity()).requestReadPermission();
            return;
        }
        favStatus = isFav;

        mView.showProgress();
        if (isFav == 0)
            mFavCall = MyApplication.get(mContext).getApiService()
                    .addTofav(new FavouriteRequestModel(user, deviceId, 1, itemID));
        else
            mFavCall = MyApplication.get(mContext).getApiService()
                    .removeFromfav(new FavouriteRequestModel(user, deviceId, 1, itemID));
        mFavCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                try {
                    MessageResultModel model = response.body().getResult();
                    if (model.getSuccess())
                        callback.favCallback(favStatus == 1 ? 0 : 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.favCallback(favStatus);
                mView.hideProgress();
            }
        });

    }

    @Override
    public void openLogin() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment(), "login_fragment")
                .addToBackStack(null).commit();
    }
}
