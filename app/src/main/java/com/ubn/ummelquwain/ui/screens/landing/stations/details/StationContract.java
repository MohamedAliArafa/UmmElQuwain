package com.ubn.ummelquwain.ui.screens.landing.stations.details;

import com.ubn.ummelquwain.models.response.Station.StationResultModel;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public interface StationContract {
    interface ModelView {
        void updateUI(StationResultModel model);

        void showProgress();

        void hideProgress();

        void setupViewPager();

        void setupTabLayout();
    }

    interface UserAction {
        void getData();

        void openLogin();

        void playStream();

        void setFav(int itemID, int isFav, StationContract.adapterCallback callback);
    }

    interface adapterCallback {
        void favCallback(int fav);
    }
}
