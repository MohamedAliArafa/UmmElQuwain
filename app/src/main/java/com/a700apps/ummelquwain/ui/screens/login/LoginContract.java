package com.a700apps.ummelquwain.ui.screens.login;

import com.a700apps.ummelquwain.models.User;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public interface LoginContract {
    interface UserAction {
        void socialLogin(User user);
    }
}
