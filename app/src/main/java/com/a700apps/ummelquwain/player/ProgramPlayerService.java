package com.a700apps.ummelquwain.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.a700apps.ummelquwain.utilities.Constants;

import java.io.IOException;

import io.realm.Realm;

/**
 * Created by mohamed.arafa on 8/29/2017.
 */

public class ProgramPlayerService extends Service {

    private final String LOG_TAG = "NotificationService";
    Notification status;
    private MediaPlayer player;
    private IBinder mBinder = new ServiceBinder();
    private ProgramResultModel mModel;

    public boolean isPreparing = false;
    private Realm mRealm;


    @Override
    public void onDestroy() {
        player.reset();
        player.release();
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(false);
        mRealm.commitTransaction();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRealm = Realm.getDefaultInstance();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener((mp, percent) -> Log.i("Persent", String.valueOf(percent)));
        player.setOnCompletionListener(mp -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
            Log.i("Completed", "true");

            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_paly_liste);
        });
        player.setOnErrorListener((mp, what, extra) -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
            Log.i("Error", "true");
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_paly_liste);
            return false;
        });
        // TODO Auto-generated method stub
        player.setOnPreparedListener(mediaPlayer -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(true);
            mRealm.commitTransaction();
//            showNotification(mModel.getProgramName(), mModel.getBroadcasterName(), mModel.getProgramImage());
            Log.i("Prepared", "true");
            mediaPlayer.start();
        });
    }

    public void preparePlayer(ProgramResultModel model) {
//        showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationImage());
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(false);
        mRealm.commitTransaction();

        mModel = model;
        if (!model.isPlaying()) {
            mModel = model;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(true);
            mRealm.commitTransaction();
            player.reset();
            try {
                player.setDataSource(mModel.getAudioProgramLink());
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.prepareAsync();
            isPreparing = true;
        }
    }

    public void togglePlay(ProgramResultModel station) {
        Log.i(LOG_TAG, "Clicked Play");
        mModel = station;
        if (player.isPlaying()) {
            player.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_active);
        } else {
            try {
                player.reset();
                player.setDataSource(station.getAudioProgramLink());
                player.prepareAsync();
                if (views != null)
                    views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
            } catch (IOException e) {
                if (views != null)
                    views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_active);
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            ProgramPlayerService model = intent.getParcelableExtra(Constants.STATION_INTENT_SERVICE_KEY);
            try {
                player.setDataSource(mModel.getAudioProgramLink());
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this.getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            togglePlay(mModel);
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }


    RemoteViews views;

    private void showNotification(String stationName, String programName, String stationImage) {
        // Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(), R.layout.status_bar);

        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);

        views.setTextViewText(R.id.status_bar_track_name, stationName);
        views.setTextViewText(R.id.status_bar_artist_name, programName);

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.logo;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    public class ServiceBinder extends Binder {
        public ProgramPlayerService getService() {
            return ProgramPlayerService.this;
        }
    }
}
