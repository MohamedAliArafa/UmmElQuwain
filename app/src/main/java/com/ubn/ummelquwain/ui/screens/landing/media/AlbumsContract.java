package com.ubn.ummelquwain.ui.screens.landing.media;

import com.ubn.ummelquwain.models.response.Albums.AlbumResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public interface AlbumsContract {
    interface ModelView {
        void updateUI(List<AlbumResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void openDetails(int albumID, String albumDesc);

        void search(String keyword);
    }
}
