package com.ubn.ummelquwain.ui.screens.landing.more.events.details;

import com.ubn.ummelquwain.models.response.Events.EventResultModel;

/**
 * Created by mohamed.arafa on 9/7/2017.
 */

public interface EventDetailContract {
    interface ModelView {
        void updateUI(EventResultModel models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
    }
}
