package com.a700apps.ummelquwain.ui.screens.landing.stations;

import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.player.PlayerCallback;

import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public interface StationsContract {
    interface View {
        void updateUI(RealmResults<StationResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void openDetails(int stationID);

        void playStream(StationResultModel station, PlayerCallback callback);

        void search(String keyword);
        void setFav(int itemID, int isFav, adapterCallback callback);
        void openLogin();
    }

    interface adapterCallback {
        void favCallback(int fav);
    }
}
