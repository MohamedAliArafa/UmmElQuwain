package com.ubn.ummelquwain.player;

import android.content.Context;
import android.content.Intent;

import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.utilities.Constants;

/*
 * Created by mohamed.arafa on 9/24/2017.
 */

public class Player {

    private Context mContext;
    //    private ServiceConnection mServiceConnection;
    private StationPlayerService mStationPlayerService;
    private ProgramPlayerService mProgramPlayerService;
    private boolean isServiceStarted;
    private boolean isProgramServiceStarted;
    private boolean isStationServiceStarted;
    private Intent mStationServiceIntent, mProgramServiceIntent;

    public Player(Context mContext) {
        this.mContext = mContext;
        mStationServiceIntent = new Intent(mContext, StationPlayerService.class);
        mStationServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

        mProgramServiceIntent = new Intent(mContext, ProgramPlayerService.class);
        mProgramServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
    }

    public void playStream(StationResultModel station) {
        mStationServiceIntent.setAction(Constants.ACTION.PLAY_ACTION);
        mStationServiceIntent.putExtra("MODEL_ID", station.getStationID());
        mContext.startService(mStationServiceIntent);
        mProgramServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mProgramServiceIntent);
//
//        if (isProgramServiceStarted) {
//            mContext.unbindService(mServiceConnection);
//            mServiceConnection = null;
//            isProgramServiceStarted = false;
//        }
//
//        if (mServiceConnection == null) {
//            mServiceConnection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                    StationPlayerService.ServiceBinder binder = (StationPlayerService.ServiceBinder) iBinder;
//                    mStationPlayerService = binder.getService();
//                    mStationPlayerService.preparePlayer(station);
        isServiceStarted = true;
        isStationServiceStarted = true;
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName componentName) {
//                    isServiceStarted = false;
//                    isStationServiceStarted = false;
//                }
//            };
//        } else {
//            if (mStationPlayerService != null) {
//                if (station.isPlaying())
//                    mStationPlayerService.preparePlayer(station);
//                else
//                    mStationPlayerService.preparePlayer(station);
//            }
//        }
//        mContext.bindService(mStationServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void playStream(ProgramResultModel program) {
        mProgramServiceIntent.setAction(Constants.ACTION.PLAY_ACTION);
        mProgramServiceIntent.putExtra("MODEL_ID", program.getProgramID());
        mContext.startService(mProgramServiceIntent);
        mStationServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mStationServiceIntent);
//
//        if (isStationServiceStarted){
//            mContext.unbindService(mServiceConnection);
//            mServiceConnection = null;
//            isStationServiceStarted = false;
//        }
//
//        if (mServiceConnection == null) {
//            mServiceConnection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                    ProgramPlayerService.ServiceBinder binder = (ProgramPlayerService.ServiceBinder) iBinder;
//                    mProgramPlayerService = binder.getService();
//                    mProgramPlayerService.preparePlayer(station);
//                    isServiceStarted = true;
//                    isProgramServiceStarted = true;
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName componentName) {
//                    isServiceStarted = false;
//                    isProgramServiceStarted = false;
//                }
//            };
//        } else {
//            if (mProgramPlayerService != null) {
//                if (station.isPlaying())
//                    mProgramPlayerService.preparePlayer(station);
//                else
//                    mProgramPlayerService.preparePlayer(station);
//            }
//        }
//        mContext.bindService(mProgramServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
