package com.a700apps.ummelquwain.ui.screens.landing.programs;

import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public interface ProgramsContract {
    interface View {
        void updateUI(RealmResults<ProgramResultModel> models);
        void showProgress();
        void hideProgress();
    }

    interface UserAction {
        void getData();
        void openDetails(int programID);
        void playStream(ProgramResultModel station);
        void search(String keyword);
    }
}
