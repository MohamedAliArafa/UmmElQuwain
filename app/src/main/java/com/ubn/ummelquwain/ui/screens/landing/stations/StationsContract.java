package com.ubn.ummelquwain.ui.screens.landing.stations;

import android.view.View;

import com.ubn.ummelquwain.models.response.Station.StationResultModel;

import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public interface StationsContract {
    interface ModelView {
        void updateUI(RealmResults<StationResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();

        void openDetails(int stationID, View viewShared);

        void playStream(StationResultModel station);

        void search(String keyword);
        void setFav(int itemID, int isFav, adapterCallback callback);
        void openLogin();
    }

    interface adapterCallback {
        void favCallback(int fav);
    }

    interface playerCallback {
        void onStart();
        void onPrepared();
        void onError();
        void onComplete();
        void onPlay();
        void onPause();
        void onStop();
    }
}
