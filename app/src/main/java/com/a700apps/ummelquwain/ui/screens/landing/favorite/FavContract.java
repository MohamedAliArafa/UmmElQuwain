package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public interface FavContract {
    interface View {
        void updateSponsorUI(List<SponsorResultModel> models);
        void updateFavUI(List<StationResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getStationData();
        void getSponsorData();
        void openDetails(int albumID);
    }
}
