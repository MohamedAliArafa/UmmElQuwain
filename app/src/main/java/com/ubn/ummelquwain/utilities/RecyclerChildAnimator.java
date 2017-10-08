package com.ubn.ummelquwain.utilities;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mohamed.arafa on 10/8/2017.
 */

public class RecyclerChildAnimator extends DefaultItemAnimator{

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
    }
}
