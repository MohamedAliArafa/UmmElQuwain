package com.a700apps.ummelquwain.ui.screens.landing;

import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationContract;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public interface LandingContract {

    interface ModelView {
        void setupViewPager();

        void setupTabLayout();

        boolean moveToHome();

        void showPlayer(StationResultModel model);

        void showPlayer(ProgramResultModel model);

        void hidePlayer();
    }

    interface UserAction {

        void playStream(StationResultModel model);

        void playStream(ProgramResultModel model);

        void setFav(int itemID, int isFav, StationContract.adapterCallback callback);

        void shareFromPlayer(StationResultModel model);

        void shareFromPlayer(ProgramResultModel model);

        void addComment(ProgramResultModel model);
    }
}
