package com.a700apps.ummelquwain.utilities;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public interface DrawableClickListener {

    public static enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}

    public void onClick(DrawablePosition target);
}