package com.ubn.ummelquwain.ui.screens.login;

import com.ubn.ummelquwain.models.User;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public interface LoginContract {
    interface UserAction {
        void socialLogin(User user);
    }
}
