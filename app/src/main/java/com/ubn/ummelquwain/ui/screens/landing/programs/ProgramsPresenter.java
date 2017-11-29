package com.ubn.ummelquwain.ui.screens.landing.programs;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.request.StationsRequestModel;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.models.response.program.ProgramsModel;
import com.ubn.ummelquwain.player.Player;
import com.ubn.ummelquwain.ui.screens.landing.programs.details.ProgramFragment;
import com.ubn.ummelquwain.utilities.DetailsTransition;
import com.ubn.ummelquwain.utilities.Utility;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ubn.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramsPresenter implements ProgramsContract.UserAction, LifecycleObserver, Callback<ProgramsModel> {
    private final Player mPlayer;
    private Context mContext;
    private ProgramsContract.ModelView mView;
    private FragmentManager mFragmentManager;
    private Call<ProgramsModel> mProgramCall;
    private RealmResults<ProgramResultModel> mModel;
    private Realm mRealm;
    private boolean isDataCalled = false;

    public ProgramsPresenter(Context mContext, ProgramsContract.ModelView mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
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
        RealmResults<ProgramResultModel> query = mRealm.where(ProgramResultModel.class).findAll().sort("programID");
        mModel = query;
        if (query.isLoaded() && !query.isEmpty()) {
            mView.hideProgress();
            mView.updateUI(mModel);
        }
        String user = MyApplication.get(mContext).getUser();
        String deviceId = MyApplication.get(mContext).getDeviceID();
        if (user.equals("-1"))
            user = deviceId;
        Log.e("user" ,user);
        mProgramCall = MyApplication.get(mContext).getApiService()
                .getAllPrograms(new StationsRequestModel(MyApplication.get(mContext).getLanguage(), user));
        if (!isDataCalled)
            mProgramCall.enqueue(this);
        isDataCalled = true;
    }

    @Override
    public void openDetails(int programID, View viewShared) {
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
                PROGRAM_FRAGMENT_KEY + String.valueOf(programID), 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            ProgramFragment fragment = ProgramFragment.newInstance(programID);
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addSharedElement(viewShared, viewShared.getTransitionName());
            ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(programID));
            ft.commit();
        }
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
        if (query.isLoaded()) {
            mView.hideProgress();
            mModel.addChangeListener(programResultModels -> mView.updateUI(mModel));
            mView.updateUI(mModel);
        }
    }

    @Override
    public void onResponse(Call<ProgramsModel> call, Response<ProgramsModel> response) {
        try {
            Utility.addProgramsToRealm(response.body().getResult(),
                    () -> {
                        mModel = mRealm.where(ProgramResultModel.class).findAll().sort("programID");
                        mView.updateUI(mModel);
                    });
            mModel.addChangeListener(programResultModels -> mView.updateUI(mModel));
        } catch (Exception e) {
            mView.showProgress();
            e.printStackTrace();
        }
        isDataCalled = false;
        mView.hideProgress();
    }

    @Override
    public void onFailure(Call<ProgramsModel> call, Throwable t) {
        Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        Log.d(this.getClass().getSimpleName(), "Internet Fail");
        t.printStackTrace();
        mView.hideProgress();
        isDataCalled = false;
    }
}
