package com.ubn.ummelquwain.utilities;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class SwipeToDismissHelper extends GestureDetector.SimpleOnGestureListener {

    private FragmentManager mFragmentManager;

    public SwipeToDismissHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onContextClick(MotionEvent e) {
        return super.onContextClick(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.i("onFling", "onFling has been called!");
        final int SWIPE_MIN_DISTANCE = 120;
        final int SWIPE_MAX_OFF_PATH = 250;
        final int SWIPE_THRESHOLD_VELOCITY = 200;
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.i("onFling", "Right to Left");
                mFragmentManager.popBackStack();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.i("onFling", "Left to Right");
                mFragmentManager.popBackStack();
            }
        } catch (Exception e) {
            // nothing
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
