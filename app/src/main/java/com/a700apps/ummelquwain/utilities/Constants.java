package com.a700apps.ummelquwain.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.a700apps.ummelquwain.R;

/**
 * Created by mohamed.arafa on 9/18/2017.
 */

public class Constants {
    public final static  String STATION_INTENT_SERVICE_KEY = "station_intent_extra";
    public static String CALLBACK_INTENT_SERVICE_KEY = "callback_intent_extra";
    public final static int REQUEST_READ_PHONE_PERMISSION = 1310;
    public final static int REQUEST_READ_CALENDER_PERMISSION = 1311;


    public interface ACTION {
        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
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
