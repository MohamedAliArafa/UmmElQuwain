package com.a700apps.ummelquwain.ui.screens.landing.stations;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import com.a700apps.ummelquwain.player.PlayerService;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.a700apps.ummelquwain.utilities.Constants;
import com.a700apps.ummelquwain.utilities.Utility;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private PlayerService mPlayerService;
    private ServiceConnection mServiceConnection;
    private Intent mServiceIntent;

    private static boolean isServiceStarted = false;

    public StationsPresenter(Context mContext, StationsFragment mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        mServiceIntent = new Intent(mContext, PlayerService.class);
        mServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        //Invoke it #onDestroy
        if (isServiceStarted) {
            mPlayerService.stopForeground(true);
            mPlayerService.stopSelf();
            mContext.unbindService(mServiceConnection);
            isServiceStarted = false;
        }
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
                            () -> mModel.addChangeListener(stationResultModels -> {
                        mView.updateUI(mModel);
                    }));
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

    @Override
    public void openDetails(int stationID) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, StationFragment.newInstance(stationID)).commit();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        //Invoke it #onDestroy
        if (isServiceStarted) {
            mContext.unbindService(mServiceConnection);
            isServiceStarted = false;
        }
    }

    @Override
    public void playStream(StationResultModel station) {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    PlayerService.ServiceBinder binder = (PlayerService.ServiceBinder) iBinder;
                    mPlayerService = binder.getService();
                    mPlayerService.preparePlayer(station);
                    isServiceStarted = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceStarted = false;
                }
            };
        } else {
            if (mPlayerService != null) {
                if (station.isPlaying())
                    mPlayerService.togglePlay(station);
                else
                    mPlayerService.preparePlayer(station);
            }
        }
        mContext.bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
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
        if (query.isLoaded() && !query.isEmpty()) {
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
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(response.body().getResult());
                    mRealm.commitTransaction();
                    mView.updateUI(mModel);
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
