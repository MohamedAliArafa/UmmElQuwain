package com.a700apps.ummelquwain.player;

/**
 * Created by mohamed.arafa on 9/20/2017.
 */

public interface RadioListener {

    void onRadioLoading();

    void onRadioConnected();

    void onRadioStarted();

    void onRadioStopped();

    void onMetaDataReceived(String s, String s2);

    void onError();
}
