package com.a700apps.ummelquwain.ui.screens.landing.more.news.details;

import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface NewsDetailContract {
    interface View {
        void updateUI(NewsBarResultModel model);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
    }
}
