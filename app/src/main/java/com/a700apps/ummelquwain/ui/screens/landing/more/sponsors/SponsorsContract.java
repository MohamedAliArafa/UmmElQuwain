package com.a700apps.ummelquwain.ui.screens.landing.more.sponsors;

import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public interface SponsorsContract {
    interface View {
        void showProgress();
        void hideProgress();
        void updateUI(List<SponsorResultModel> mModel);
    }
    interface UserAction {
        void getData();
    }
}
