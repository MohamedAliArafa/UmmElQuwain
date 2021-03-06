package com.ubn.ummelquwain.ui.screens.landing.more.joinus;

import android.content.Intent;

import com.ubn.ummelquwain.models.request.JoinUsRequestModel;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public interface JoinUsContract {
    interface ModelView {

        void showProgress();

        void hideProgress();

        void setProgress(int percentage);
    }

    interface fileCallback {
        String onFileUploaded(String fileName);
    }

    interface UserAction {
        void uploadFile(Intent data, JoinUsContract.fileCallback callback);

        void join(JoinUsRequestModel data);
    }
}
