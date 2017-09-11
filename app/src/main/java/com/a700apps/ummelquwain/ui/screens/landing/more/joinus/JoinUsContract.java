package com.a700apps.ummelquwain.ui.screens.landing.more.joinus;

import android.content.Intent;

import com.a700apps.ummelquwain.models.request.JoinUsRequestModel;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface JoinUsContract {
    interface View {

        void showProgress();

        void hideProgress();
    }

    interface UserAction {
        String uploadFile(Intent data);
        void join(JoinUsRequestModel data);
    }
}
