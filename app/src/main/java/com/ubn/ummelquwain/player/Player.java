package com.ubn.ummelquwain.player;

import android.content.Context;
import android.content.Intent;

import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.utilities.Constants;

import static com.ubn.ummelquwain.utilities.Constants.MODEL_ID;

/*
 * Created by mohamed.arafa on 9/24/2017.
 */

public class Player {

    private Context mContext;
    private Intent mStationServiceIntent, mProgramServiceIntent;

    public Player(Context mContext) {
        this.mContext = mContext;
        mProgramServiceIntent = new Intent(mContext, ProgramPlayerService.class);
        mStationServiceIntent = new Intent(mContext, StationPlayerService.class);
    }

    public void playStream(StationResultModel station) {
        mProgramServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mProgramServiceIntent);
        mStationServiceIntent.setAction(Constants.ACTION.PLAY_ACTION);
        mStationServiceIntent.putExtra(MODEL_ID, station.getStationID());
        mContext.startService(mStationServiceIntent);
    }

    public void playStream(ProgramResultModel program) {
        mStationServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mStationServiceIntent);
        mProgramServiceIntent.setAction(Constants.ACTION.PLAY_ACTION);
        mProgramServiceIntent.putExtra(MODEL_ID, program.getProgramID());
        mContext.startService(mProgramServiceIntent);
    }
}
