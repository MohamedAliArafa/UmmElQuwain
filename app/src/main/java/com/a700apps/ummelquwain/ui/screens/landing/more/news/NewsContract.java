package com.a700apps.ummelquwain.ui.screens.landing.more.news;

import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;

import java.util.List;

/*
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface NewsContract {

    interface ModelView {

        void updateUI(List<NewsBarResultModel> model);

        void showProgress();

        void hideProgress();

    }

    public interface UserAction {

        void getData();

        void openDetails(int newsID);

    }
}
