package com.a700apps.ummelquwain.ui.screens.landing.more.events;

import com.a700apps.ummelquwain.models.response.Events.EventResultModel;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface EventsContract {
    interface ModelView {
        void updateUI(List<EventResultModel> models);
        void showProgress();
        void hideProgress();
        boolean requestReadPermission();
    }

    interface UserAction {
        void getData();
        void openDetails(EventResultModel eventID);

        void shareEvent(EventResultModel model);

        void addToCalender(EventResultModel model);
    }
}
