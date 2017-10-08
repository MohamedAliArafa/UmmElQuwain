package com.ubn.ummelquwain.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.main.MainActivity;
import com.ubn.ummelquwain.utilities.Constants;

import java.io.IOException;

import io.realm.Realm;

/**
 * Created by mohamed.arafa on 8/29/2017.
 */

public class StationPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

    private final String LOG_TAG = "NotificationService";
    private static final String ACTION_PLAY = "PLAY";

    private MediaPlayer mPlayer = null;
    private int mBufferPosition;
    private IBinder mBinder = new ServiceBinder();
    private static StationResultModel mModel;
    private Picasso mPicasso;
    private static StationPlayerService mInstance = null;

    public boolean isPreparing = false;
    private Realm mRealm;

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Toast.makeText(getApplicationContext(), "Buffering (" + i + ") ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        isPreparing = false;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Stopped);
        mRealm.commitTransaction();
        Log.i("Error", "true");
        mState = State.Stopped;
        stopForeground(true);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mState = State.Playing;
        isPreparing = false;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Playing);
        mRealm.commitTransaction();
        Log.i("Prepared", "true");
        mPlayer.start();
    }

    // indicates the state our service:
    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Playing, // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused
        // playback paused (media player ready!)
    }

    State mState = State.Retrieving;

    NotificationManager mNotificationManager;
    Notification mNotification = null;
    final int NOTIFICATION_ID = 1;

    public MediaPlayer getMediaPlayer() {
        return mPlayer;
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null)
            mPlayer.release();
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Stopped);
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
        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mRealm = Realm.getDefaultInstance();
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPicasso = MyApplication.get(this).getPicasso();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnBufferingUpdateListener((mp, percent) -> Log.i("Percent", String.valueOf(percent)));
        mPlayer.setOnCompletionListener(mp -> {
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Playing);
            mRealm.commitTransaction();
            Toast.makeText(getApplicationContext(), "Buffering", Toast.LENGTH_SHORT).show();
            if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
                mPlayer.start();
                mState = State.Playing;
            } else if (mState.equals(State.Retrieving)) {
                initMediaPlayer();
            }
        });
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnPreparedListener(this);
    }

    public void preparePlayer(StationResultModel model) {
//        showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationImage());
        mRealm.beginTransaction();
        if (mModel != null)
            if (mModel.getStationID() != model.getStationID()) {
                mModel.setPlaying(StationResultModel.State.Stopped);
                mModel = model;
                initMediaPlayer();
            }
        mRealm.commitTransaction();
        mModel = model;
        showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationLogo());
        if (model.isPlaying() != StationResultModel.State.Playing && model.isPlaying() != StationResultModel.State.Buffering) {
            mModel = model;
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Buffering);
            mRealm.commitTransaction();
            startMusic();
//            mPlayer.reset();
//            try {
//                mPlayer.setDataSource(mModel.getStreamLink());
//                mPlayer.prepareAsync();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            isPreparing = true;
        } else {
//            mPlayer.pause();
            pauseMusic();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Paused);
            mRealm.commitTransaction();
        }
    }

    public void pauseMusic() {
        if (mState.equals(State.Playing)) {
            mPlayer.pause();
            mState = State.Paused;
            updateNotification(mModel.getStationName() + "(paused)");
        }
    }

    public void startMusic() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            mPlayer.start();
            mState = State.Playing;
            updateNotification(mModel.getStationName() + "(playing)");
        } else if (mState.equals(State.Retrieving)) {
            initMediaPlayer();
        }
    }

    /**
     * Updates the notification.
     */
    void updateNotification(String text) {
        // Notify NotificationManager of new intent
    }

    public boolean isPlaying() {
        return mState.equals(State.Playing);
    }

    public static StationPlayerService getInstance() {
        return mInstance;
    }

    public static void setSong(StationResultModel model) {
        mModel = model;
    }

    public String getSongTitle() {
        return mModel.getStationName();
    }

    public String getSongPicUrl() {
        return mModel.getStationLogo();
    }

    void setUpAsForeground(String text) {
        PendingIntent pi =
                PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
//        mNotification = new Notification();
//        mNotification.tickerText = text;
//        mNotification.icon = R.drawable.ic_mshuffle_icon;
//        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
//        mNotification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), text, pi);

        if (mModel.isPlaying() == StationResultModel.State.Playing)
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
        else
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);

        mModel.addChangeListener(realmModel -> {
            if (mModel.isPlaying() != StationResultModel.State.Playing)
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
            else
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        });

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        mNotification = new Notification.Builder(this).build();
        mNotification.contentView = mRemoteViews;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.icon = R.drawable.logo;
        mNotification.contentIntent = pendingIntent;
        mPicasso.load(mModel.getStationLogo()).into(mRemoteViews, R.id.status_bar_album_art, Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    private void initMediaPlayer() {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(mModel.getStreamLink());
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mState = State.Preparing;
    }

    public void togglePlay(StationResultModel station) {
        Log.i(LOG_TAG, "Clicked Play");
        mModel = station;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Stopped);
            mRealm.commitTransaction();
        } else {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(station.getStreamLink());
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            int id = intent.getIntExtra("MODEL_ID", 0);
            StationResultModel model = mRealm.where(StationResultModel.class).equalTo("stationID", id).findFirst();
            if (model != null)
                preparePlayer(model);
        } else if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            try {
                mPlayer.setDataSource(mModel.getStreamLink());
            } catch (IllegalArgumentException | IOException | IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepareAsync();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mState = State.Preparing;
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            int id = intent.getIntExtra("MODEL_ID", 0);
            StationResultModel model = mRealm.where(StationResultModel.class).equalTo("stationID", id).findFirst();
            if (model != null)
                preparePlayer(model);
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
            mPlayer.reset();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Stopped);
            mRealm.commitTransaction();
            stopForeground(true);
            stopSelf();
//            this.onDestroy();
        }
        return START_STICKY;
    }

    RemoteViews mRemoteViews;

    private void showNotification(String stationName, String programName, String stationImage) {
        // Using RemoteViews to bind custom layouts into Notification
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.status_bar);

        // showing default album image
//        mRemoteViews.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.status_bar_album_art, View.GONE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, StationPlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, StationPlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, StationPlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, StationPlayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);

        mRemoteViews.setTextViewText(R.id.status_bar_track_name, stationName);
        mRemoteViews.setTextViewText(R.id.status_bar_artist_name, programName);

        if (mModel.isPlaying() == StationResultModel.State.Playing)
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
        else
            mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);

        mModel.addChangeListener(realmModel -> {
            if (mModel.isPlaying() == StationResultModel.State.Playing || mModel.isPlaying() == StationResultModel.State.Buffering)
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_puss);
            else
                mRemoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_paly_liste);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        });

        mNotification = new Notification.Builder(this).build();
        mNotification.contentView = mRemoteViews;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.icon = R.drawable.logo;
        mNotification.contentIntent = pendingIntent;
        mPicasso.load(stationImage).into(mRemoteViews, R.id.status_bar_album_art, Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
    }

    public class ServiceBinder extends Binder {
        public StationPlayerService getService() {
            return StationPlayerService.this;
        }
    }

    private class BecomingNoisyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                // Pause the playback
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mRealm.beginTransaction();
                    if (mModel != null)
                        mModel.setPlaying(StationResultModel.State.Stopped);
                    mRealm.commitTransaction();
                }
            }
        }
    }
}
