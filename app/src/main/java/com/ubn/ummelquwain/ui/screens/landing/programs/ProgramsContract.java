package com.ubn.ummelquwain.ui.screens.landing.programs;

import com.ubn.ummelquwain.models.response.program.ProgramResultModel;

import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public interface ProgramsContract {
    interface ModelView {
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
