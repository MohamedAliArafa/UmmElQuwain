package com.ubn.ummelquwain.ui.screens.landing.media.albums.media;

import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/4/2017.
 */

public interface MediaContract {
    interface ModelView {
        void updateUI(List<MediaResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void openDetails(List<MediaResultModel> media, int position);
    }
}
