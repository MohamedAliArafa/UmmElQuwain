package com.ubn.ummelquwain.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ubn.ummelquwain.R;

/**
 * Created by mohamed.arafa on 9/18/2017.
 */

public class Constants {
    public final static String STATION_INTENT_SERVICE_KEY = "station_intent_extra";
    public static String CALLBACK_INTENT_SERVICE_KEY = "callback_intent_extra";
    public final static int REQUEST_READ_PHONE_PERMISSION = 1310;
    public final static int REQUEST_READ_CALENDER_PERMISSION = 1311;
    public static final int REQUEST_READ_STORAGE_PERMISSION = 1312;
    public static final int REQUEST_PHONE_CALL_PERMISSION = 1313;

    public final static String POSITION_KEY = "position_list";
    public final static String LOGIN_FRAGMENT_KEY = "login_fragment_key";
    public final static String LANDING_FRAGMENT_KEY = "landing_fragment_key";
    public final static String STATION_FRAGMENT_KEY = "station_fragment_key";
    public final static String PROGRAM_FRAGMENT_KEY = "program_fragment_key";
    public static String Broadcast_PLAY_NEW_AUDIO = "broadcast_play_new_audio";


    public interface ACTION {
        String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_launcher_round, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}