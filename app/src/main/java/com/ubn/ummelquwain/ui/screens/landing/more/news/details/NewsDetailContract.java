package com.ubn.ummelquwain.ui.screens.landing.more.news.details;

import com.ubn.ummelquwain.models.response.NewsBar.NewsBarResultModel;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface NewsDetailContract {
    interface ModelView {
        void updateUI(NewsBarResultModel model);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void shareNews(NewsBarResultModel model);
    }
}
