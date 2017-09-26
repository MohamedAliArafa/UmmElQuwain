package com.a700apps.ummelquwain.ui.screens.landing.stations.details.info;

import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public interface StationInfoContract {

    interface ModelView {

    }

    interface UserAction {
       void openSite(String url);
       void openProgram(ProgramResultModel model);
    }
}
