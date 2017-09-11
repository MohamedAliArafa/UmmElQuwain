package com.a700apps.ummelquwain.ui.screens.landing.programs.details;

import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

/*
 * Created by mohamed.arafa on 9/10/2017.
 */

public interface ProgramContract {
    interface View {
        void updateUI(ProgramResultModel model);

        void showProgress();

        void hideProgress();

        void setupViewPager();

        void setupTabLayout();
    }

    interface UserAction {
        void getData();
    }
}
