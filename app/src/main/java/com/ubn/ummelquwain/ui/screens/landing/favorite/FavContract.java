package com.ubn.ummelquwain.ui.screens.landing.favorite;

import android.view.View;

import com.ubn.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.landing.stations.StationsContract;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public interface FavContract {
    interface ModelView {
        void updateSponsorUI(List<SponsorResultModel> models);
        void updateFavUI(RealmResults<StationResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getStationData();
        void getSponsorData();

        void playStream(StationResultModel station);

        void openDetails(int stationID, View viewShared);
        void setFav(int itemID, int isFav, StationsContract.adapterCallback callback);

        void openLogin();
    }
}
