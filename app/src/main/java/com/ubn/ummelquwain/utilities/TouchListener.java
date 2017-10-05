package com.ubn.ummelquwain.utilities;

import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;

/*
 * Created by mohamed.arafa on 9/25/2017.
 */

public class TouchListener implements View.OnTouchListener {

    private View slidingView;

    private int initHeight;
    private float initPos;
    private int parentHeight;
    private ConstraintLayout.LayoutParams params;

    public TouchListener(ConstraintLayout slidingView) {
        this.slidingView = slidingView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (params == null) {
            params = (ConstraintLayout.LayoutParams) slidingView.getLayoutParams();
            parentHeight = slidingView.getHeight();
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: //get initial state
                initHeight = slidingView.getHeight();
                initPos = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE: //do the sliding
                float dPos = initPos - event.getRawY();
                if ((initHeight + dPos) > parentHeight)
                    params.height = parentHeight;
                else if ((initHeight + dPos) < parentHeight / 2)
                    params.height = Math.round( parentHeight / 2);
                else
                    params.height = Math.round(initHeight + dPos);
                slidingView.requestLayout(); //refresh layout
                break;
        }
        v.performClick();
        return false;
    }
}
