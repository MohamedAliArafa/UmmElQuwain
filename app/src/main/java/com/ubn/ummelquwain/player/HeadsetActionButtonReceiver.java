package com.ubn.ummelquwain.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import java.util.Timer;

/**
 * Created by mohamed.arafa on 10/11/2017.
 */

public class HeadsetActionButtonReceiver extends BroadcastReceiver {

    public static Delegate mDelegate;

    private static AudioManager mAudioManager;
    private static ComponentName mRemoteControlResponder;

    private static int doublePressSpeed = 300; // double keypressed in ms
    private static Timer doublePressTimer;
    private static int counter;

    public interface Delegate {
        void onMediaButtonSingleClick();

        void onMediaButtonDoubleClick();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || mDelegate == null || !Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction()))
            return;
        KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
        if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN) return;
        mDelegate.onMediaButtonSingleClick();
    }

    public static void register(final Context context, Delegate delegate) {
        mDelegate = delegate;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mRemoteControlResponder = new ComponentName(context, HeadsetActionButtonReceiver.class);
        mAudioManager.registerMediaButtonEventReceiver(mRemoteControlResponder);
    }

    public static void unregister() {
        if (mRemoteControlResponder != null)
            mAudioManager.unregisterMediaButtonEventReceiver(mRemoteControlResponder);
        if (doublePressTimer != null) {
            doublePressTimer.cancel();
            doublePressTimer = null;
        }
    }
}
