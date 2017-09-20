package com.a700apps.ummelquwain.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.a700apps.ummelquwain.utilities.Constants;

import java.io.IOException;

/**
 * Created by mohamed.arafa on 8/29/2017.
 */

public class PlayerService extends Service {

    private final String LOG_TAG = "NotificationService";
    Notification status;
    private MediaPlayer player;
    private IBinder mBinder = new ServiceBinder();
    private StationResultModel mModel;

    private boolean isRetry = false;
    public boolean isPreparing = false;

    private String mStreamLink = "http://ca.rcast.net:8058/;stream.mp3";

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
//        if (player.isPlaying()) {
//            player.stop();
//            callback.send(0, new Bundle());
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    ResultReceiver callback;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener((mp, percent) -> Log.i("Persent", String.valueOf(percent)));
        player.setOnCompletionListener(mp -> {
            isPreparing = false;
            Log.i("Completed", "true");
//            Toast.makeText(PlayerService.this.getApplicationContext(), R.string.toas_completed, Toast.LENGTH_SHORT).show();
            if (isRetry)
                try {
                    player.setDataSource(mStreamLink);
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_paly_liste);
            callback.send(0, new Bundle());
        });
        player.setOnErrorListener((mp, what, extra) -> {
            isPreparing = false;
//            Toast.makeText(PlayerService.this.getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_paly_liste);
            callback.send(0, new Bundle());
            return false;
        });
        // TODO Auto-generated method stub
        player.setOnPreparedListener(mediaPlayer -> {
            isPreparing = false;
//            Toast.makeText(PlayerService.this.getApplicationContext(), R.string.toast_prepared, Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            callback.send(1, new Bundle());
        });
    }

    public void preparePlayer(StationResultModel model, PlayerCallback callback) {
//        showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationImage());
        mModel = model;
        this.callback = callback;
        if (player.isPlaying() || isPreparing) {
            player.stop();
            player.reset();
            isRetry = true;
            return;
        }
        try {
            player.setDataSource(mStreamLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
        isPreparing = true;
//        Toast.makeText(this.getApplicationContext(), R.string.toast_service_started, Toast.LENGTH_SHORT).show();
    }

    public void togglePlay(PlayerCallback callback) {
//        Toast.makeText(this.getApplicationContext(), R.string.toast_clicked_play, Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "Clicked Play");
        if (player.isPlaying()) {
//            Toast.makeText(PlayerService.this.getApplicationContext(), R.string.toast_pause, Toast.LENGTH_SHORT).show();
            player.pause();
            callback.send(0, new Bundle());
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_paly_active);
        } else {
//            Toast.makeText(PlayerService.this.getApplicationContext(), R.string.toast_start, Toast.LENGTH_SHORT).show();
            player.start();
            callback.send(1, new Bundle());
            if (views != null)
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.ic_puss);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            StationResultModel model = intent.getParcelableExtra(Constants.STATION_INTENT_SERVICE_KEY);
            callback = intent.getParcelableExtra(Constants.CALLBACK_INTENT_SERVICE_KEY);
//            showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationImage());
            try {
                player.setDataSource(mStreamLink);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this.getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
//            Toast.makeText(this.getApplicationContext(), R.string.toast_clicked_previous, Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Toast.makeText(this.getApplicationContext(), "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
            if (player.isPlaying()) {
                Toast.makeText(PlayerService.this.getApplicationContext(), "pause", Toast.LENGTH_SHORT).show();
                player.pause();
                callback.send(0, new Bundle());
                if (views != null)
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_paly_active);
            } else {
                Toast.makeText(PlayerService.this.getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
                player.start();
                callback.send(1, new Bundle());
                if (views != null)
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_puss);
            }
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
//            Toast.makeText(this.getApplicationContext(), R.string.toast_clicked_next, Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
            callback.send(1, new Bundle());
//            Toast.makeText(this.getApplicationContext(), R.string.toast_service_stoped, Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }


    RemoteViews views;
//    RemoteViews bigViews;

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
//        views.setImageViewResource(R.id.status_bar_album_art, R.drawable.logo);

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
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
