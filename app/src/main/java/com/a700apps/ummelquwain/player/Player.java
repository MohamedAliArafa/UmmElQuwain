package com.a700apps.ummelquwain.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.utilities.Constants;

/*
 * Created by mohamed.arafa on 9/24/2017.
 */

public class Player {

    private Context mContext;
    private ServiceConnection mServiceConnection;
    private StationPlayerService mStationPlayerService;
    private ProgramPlayerService mProgramPlayerService;
    private boolean isServiceStarted;
    private Intent mStationServiceIntent, mProgramServiceIntent;

    public Player(Context mContext) {
        this.mContext = mContext;
        mStationServiceIntent = new Intent(mContext, StationPlayerService.class);
        mStationServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

        mProgramServiceIntent = new Intent(mContext, ProgramPlayerService.class);
        mProgramServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
    }

    public void playStream(StationResultModel station) {
        mProgramServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mProgramServiceIntent);

        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    StationPlayerService.ServiceBinder binder = (StationPlayerService.ServiceBinder) iBinder;
                    mStationPlayerService = binder.getService();
                    mStationPlayerService.preparePlayer(station);
                    isServiceStarted = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceStarted = false;
                }
            };
        } else {
            if (mStationPlayerService != null) {
                if (station.isPlaying())
                    mStationPlayerService.preparePlayer(station);
                else
                    mStationPlayerService.preparePlayer(station);
            }
        }
        mContext.bindService(mStationServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void playStream(ProgramResultModel station) {
        mStationServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        mContext.startService(mStationServiceIntent);

        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    ProgramPlayerService.ServiceBinder binder = (ProgramPlayerService.ServiceBinder) iBinder;
                    mProgramPlayerService = binder.getService();
                    mProgramPlayerService.preparePlayer(station);
                    isServiceStarted = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceStarted = false;
                }
            };
        } else {
            if (mProgramPlayerService != null) {
                if (station.isPlaying())
                    mProgramPlayerService.preparePlayer(station);
                else
                    mProgramPlayerService.preparePlayer(station);
            }
        }
        mContext.bindService(mProgramServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
