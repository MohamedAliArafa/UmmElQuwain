package com.a700apps.ummelquwain.ui.screens.landing.programs;

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
import com.a700apps.ummelquwain.models.request.SearchRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramsModel;
import com.a700apps.ummelquwain.player.Player;
import com.a700apps.ummelquwain.ui.screens.landing.programs.details.ProgramFragment;
import com.a700apps.ummelquwain.utilities.Utility;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a700apps.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramsPresenter implements ProgramsContract.UserAction, LifecycleObserver {
    private final Player mPlayer;
    private Context mContext;
    private ProgramsContract.ModelView mView;
    private FragmentManager mFragmentManager;
    private Call<ProgramsModel> mStationsCall;
    private RealmResults<ProgramResultModel> mModel;
    private Realm mRealm;

    public ProgramsPresenter(Context mContext, ProgramsContract.ModelView mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
        mPlayer = MyApplication.get(mContext).getPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void getData() {
        mView.showProgress();
        mRealm = Realm.getDefaultInstance();
        RealmResults<ProgramResultModel> query = mRealm.where(ProgramResultModel.class).findAll().sort("programID");
        if (query.isLoaded() && !query.isEmpty()) {
            mModel = query;
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .getAllPrograms(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), MyApplication.get(mContext).getUser()));
        mStationsCall.enqueue(new Callback<ProgramsModel>() {
            @Override
            public void onResponse(@NonNull Call<ProgramsModel> call, @NonNull Response<ProgramsModel> response) {
                try {
                    Utility.addProgramsToRealm(response.body().getResult(),
                            () -> mModel.addChangeListener(stationResultModels -> {
                                mView.updateUI(mModel);
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ProgramsModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(int programID) {
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
                PROGRAM_FRAGMENT_KEY + String.valueOf(programID), 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, ProgramFragment.newInstance(programID));
            ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(programID));
            ft.commit();
        }
//        mFragmentManager.beginTransaction().addToBackStack(null)
//                .add(R.id.fragment_container, ProgramFragment.newInstance(programID)).commit();
//        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void playStream(ProgramResultModel program) {
        mPlayer.playStream(program);
    }

    @Override
    public void search(String keyword) {
        mView.showProgress();

        mRealm = Realm.getDefaultInstance();
        RealmResults<ProgramResultModel> query = mRealm.where(ProgramResultModel.class)
                .contains("programName", keyword, Case.INSENSITIVE).findAll();
        mModel = query;
        if (query.isLoaded() && !query.isEmpty()) {
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        mStationsCall = MyApplication.get(mContext).getApiService()
                .searchPrograms(new SearchRequestModel(keyword, MyApplication.get(mContext).getUser(), MyApplication.get(mContext).getLanguage()));
        mStationsCall.enqueue(new Callback<ProgramsModel>() {
            @Override
            public void onResponse(@NonNull Call<ProgramsModel> call, @NonNull Response<ProgramsModel> response) {
                try {
                    Utility.addProgramsToRealm(response.body().getResult(),
                            () -> mModel.addChangeListener(stationResultModels -> {
                                mView.updateUI(mModel);
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ProgramsModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }
}
