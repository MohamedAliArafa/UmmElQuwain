package com.ubn.ummelquwain.player;

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

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.ui.screens.main.MainActivity;
import com.ubn.ummelquwain.utilities.Constants;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

/**
 * Created by mohamed.arafa on 8/29/2017.
 */

public class ProgramPlayerService extends Service {

    private final String LOG_TAG = "NotificationService";
    Notification mNotification;
    private MediaPlayer mPlayer;
    private IBinder mBinder = new ServiceBinder();
    private ProgramResultModel mModel;
    private Picasso mPicasso;

    public boolean isPreparing = false;
    private Realm mRealm;

    @Override
    public void onDestroy() {
        mPlayer.reset();
        mPlayer.release();
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
        mPlayer = new MediaPlayer();
        mPicasso = MyApplication.get(this).getPicasso();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnBufferingUpdateListener((mp, percent) -> Log.i("Percent", String.valueOf(percent)));
        mPlayer.setOnCompletionListener(mp -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
            Log.i("Completed", "true");
        });
        mPlayer.setOnBufferingUpdateListener((mediaPlayer, i) -> {
            Log.d("Buffering", "->" + i);

        });
        mPlayer.setOnErrorListener((mp, what, extra) -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
            Log.i("Error", "true");
            return false;
        });
        // TODO Auto-generated method stub
        mPlayer.setOnPreparedListener(mediaPlayer -> {
            isPreparing = false;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(true);
            mRealm.commitTransaction();
            Log.i("Prepared", "true");
            mediaPlayer.start();
        });
    }

    public void preparePlayer(ProgramResultModel model) {
        mRealm.beginTransaction();
        if (mModel != null)
            if (mModel.getProgramID() != model.getProgramID())
                mModel.setPlaying(false);
        mRealm.commitTransaction();

        mModel = model;
        showNotification(model.getProgramName(), model.getBroadcasterName(), model.getProgramLogo());
        if (!model.isPlaying()) {
            mModel = model;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(true);
            mRealm.commitTransaction();
            mPlayer.reset();
            try {
                mPlayer.setDataSource(mModel.getAudioProgramLink());
                mPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreparing = true;
        } else {
            mPlayer.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
        }
    }

    public void togglePlay(ProgramResultModel station) {
        Log.i(LOG_TAG, "Clicked Play");
        mModel = station;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(false);
            mRealm.commitTransaction();
        } else {
            try {
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(true);
                mRealm.commitTransaction();
                mPlayer.reset();
                mPlayer.setDataSource(station.getAudioProgramLink());
                mPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                try {
                    mPlayer.setDataSource(mModel.getAudioProgramLink());
                    mPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this.getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
            } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                preparePlayer(mModel);
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
                mPlayer.reset();
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(false);
                mRealm.commitTransaction();
                stopForeground(true);
                stopSelf();
            }
        return START_STICKY;
    }

    RemoteViews mRemoteViews;

    private void showNotification(String stationName, String programName, String stationImage) {
        // Using RemoteViews to bind custom layouts into Notification
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.status_bar);

        // showing default album image
//        mRemoteViews.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, ProgramPlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, ProgramPlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, ProgramPlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, ProgramPlayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        if (mModel.isPlaying())
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
        else
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);

        mModel.addChangeListener(realmModel -> {
            if (mModel.isPlaying())
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
            else
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        });

        mRemoteViews.setTextViewText(R.id.status_bar_track_name, stationName);
        mRemoteViews.setTextViewText(R.id.status_bar_artist_name, programName);

        mNotification = new Notification.Builder(this).build();
        mNotification.contentView = mRemoteViews;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.icon = R.drawable.logo;
        mNotification.contentIntent = pendingIntent;
        mPicasso.load(stationImage).into(mRemoteViews, R.id.status_bar_album_art, Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
    }

    public class ServiceBinder extends Binder {
        public ProgramPlayerService getService() {
            return ProgramPlayerService.this;
        }
    }
}