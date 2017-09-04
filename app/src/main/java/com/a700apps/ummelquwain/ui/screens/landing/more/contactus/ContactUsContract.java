package com.a700apps.ummelquwain.ui.screens.landing.more.contactus;

import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsResultModel;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public interface ContactUsContract {
    interface View {
        void showProgress();
        void hideProgress();
        void updateUI(ContactUsResultModel mModel);
    }

    interface UserAction {
        void getData();
        void showFacebook();
        void showTwitter();
        void showInstagram();
        void showLinkedIn();
        void openMap();
    }
}
