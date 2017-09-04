package com.a700apps.ummelquwain.utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context, int itemWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / itemWidth);
    }
}
