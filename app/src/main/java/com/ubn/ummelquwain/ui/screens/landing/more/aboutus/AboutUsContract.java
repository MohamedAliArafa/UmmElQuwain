package com.ubn.ummelquwain.ui.screens.landing.more.aboutus;

import com.ubn.ummelquwain.models.response.AboutUs.AboutUsResultModel;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public interface AboutUsContract {
    interface ModelView {
        void showProgress();
        void hideProgress();
        void updateUI(AboutUsResultModel mModel);
    }
    interface UserAction {
        void getData();
    }
}
