package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsContract;

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

        void openDetails(int albumID);
        void setFav(int itemID, int isFav, StationsContract.adapterCallback callback);

        void openLogin();
    }
}
