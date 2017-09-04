package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media;

import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/4/2017.
 */

public interface MediaContract {
    interface View {
        void updateUI(List<MediaResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void openDetails(int mediaID, int mediaType);
    }
}
