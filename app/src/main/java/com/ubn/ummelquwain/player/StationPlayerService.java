package com.ubn.ummelquwain.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.main.MainActivity;
import com.ubn.ummelquwain.utilities.Constants;

import java.io.IOException;

import io.realm.Realm;

import static android.content.Intent.ACTION_MEDIA_BUTTON;

/**
 * Created by mohamed.arafa on 8/29/2017.
 */

public class StationPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, Playback.Callback {

    //Current Playing Station
    private static StationResultModel mModel;

    //Instance
    private static StationPlayerService mInstance = null;
    private final String LOG_TAG = StationPlayerService.class.getName();
    private IBinder mBinder = new ServiceBinder();

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //Notification
    final int NOTIFICATION_ID = 1;
    NotificationManager mNotificationManager;
    Notification mNotification = null;
    RemoteViews mRemoteViews;

    //State
    public boolean isPreparing = false;
    State mState = State.Retrieving;

    //Player
    private MediaPlayer mPlayer = null;
    private Playback mPlayback;

    //Third Parties
    private Picasso mPicasso;
    private Realm mRealm;

    //Phone Callback
    private boolean ongoingCall;

    public StationPlayerService() {
    }

    public static StationPlayerService getInstance() {
        return mInstance;
    }

    public static void setSong(StationResultModel model) {
        mModel = model;
    }

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
    }

    @Override
    public void onCompletion() {
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Stopped);
        mRealm.commitTransaction();
    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        switch (state) {
            case PlaybackState.STATE_PLAYING:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(StationResultModel.State.Playing);
                mRealm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
                mState = State.Playing;
                break;
            case PlaybackState.STATE_PAUSED:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(StationResultModel.State.Paused);
                mRealm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Paused", Toast.LENGTH_SHORT).show();
                mState = State.Paused;
                break;
            case PlaybackState.STATE_STOPPED:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(StationResultModel.State.Stopped);
                mRealm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
                mState = State.Stopped;
                break;
            case PlaybackState.STATE_BUFFERING:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(StationResultModel.State.Buffering);
                mRealm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Buffering", Toast.LENGTH_SHORT).show();
                mState = State.Preparing;
                break;

        }
    }

    @Override
    public void onError(String error) {
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Stopped);
        mRealm.commitTransaction();
    }

    public MediaPlayer getMediaPlayer() {
        return mPlayer;
    }

    @Override
    public void onDestroy() {
//        if (mPlayer != null)
//            mPlayer.release();
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(StationResultModel.State.Stopped);
        mRealm.commitTransaction();
        mPlayback.stop(true);
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
        callStateListener();
        try {
            initMediaSession();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mPicasso = MyApplication.get(this).getPicasso();
        mPlayback = new Playback(this);
        mPlayback.setState(PlaybackState.STATE_NONE);
        mPlayback.setCallback(this);
        mPlayback.start();
    }

    public void preparePlayer(StationResultModel model) {
        if (mModel != null)
            if (mModel.getStationID() != model.getStationID()) {
                mRealm.beginTransaction();
                mModel.setPlaying(StationResultModel.State.Stopped);
                mRealm.commitTransaction();
                mModel = model;
                mPlayback.setMedia(mModel);
                mPlayback.play();
                transportControls.play();
                return;
            }
        mModel = model;
//        showNotification(model.getStationName(), model.getCurrentProgramName(), model.getStationLogo());
        if (model.isPlaying() != StationResultModel.State.Playing && model.isPlaying() != StationResultModel.State.Buffering) {
            mModel = model;
            startMusic();
            transportControls.play();
            isPreparing = true;
        } else {
            pauseMusic();
            transportControls.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Paused);
            mRealm.commitTransaction();
        }
    }

    public void pauseMusic() {
        if (mState.equals(State.Playing)) {
            mPlayback.setMedia(mModel);
            mPlayback.pause();
            mState = State.Paused;
            updateNotification(mModel.getStationName() + "(paused)");
        } else if (mState.equals(State.Preparing) || mState.equals(State.Retrieving)) {
            mPlayback.stop(true);
            mState = State.Retrieving;
        }
    }

    public void startMusic() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            mPlayback.setMedia(mModel);
            mPlayback.play();
            updateNotification(mModel.getStationName() + "(playing)");
        } else if (mState.equals(State.Retrieving)) {
            mPlayback.setMedia(mModel);
            mPlayback.play();
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
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_STARTFOREGROUND_ACTION));
        } else if (intent.getAction().equals(ACTION_MEDIA_BUTTON)) {
            Log.i(LOG_TAG, getString(R.string.toast_media_clicked));
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            int id = intent.getIntExtra("MODEL_ID", 0);
            StationResultModel model = mRealm.where(StationResultModel.class).equalTo("stationID", id).findFirst();
            if (model != null) preparePlayer(model);
        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) {
            pauseMusic();
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
            mPlayback.stop(true);
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(StationResultModel.State.Stopped);
            mRealm.commitTransaction();
            stopForeground(true);
            stopSelf();
        }
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
//        handleIncomingActions(intent);
//        return START_STICKY;
    }

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

    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mPlayback != null) {
                            pauseMusic();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mPlayback != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                if (mModel != null)
                                    preparePlayer(mModel);
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
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

    public class ServiceBinder extends Binder {
        public StationPlayerService getService() {
            return StationPlayerService.this;
        }
    }

    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        Intent mediaButtonIntent = new Intent(ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(getApplicationContext(), MediaButtonReceiver.class);
        PendingIntent mbrIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
        mediaSession.setMediaButtonReceiver(mbrIntent);
        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                buildNotification();
            }

            @Override
            public void onPause() {
                super.onPause();
                buildNotification();
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    private void updateMetaData() {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.avatar); //replace with medias albumArt
        // Update the current metadata
        if (mModel != null)
            mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mModel.getCurrentProgramName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mModel.getCategoryName())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mModel.getStationName())
                    .build());
    }

    Bitmap largeIcon;
    NotificationCompat.Builder notificationBuilder = null;

    private void buildNotification() {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        //Build a new notification according to the current state of the MediaPlayer
        if (mState.equals(State.Playing)) {
            notificationAction = android.R.drawable.ic_media_pause;
        } else if (mState.equals(State.Paused)) {
            notificationAction = android.R.drawable.ic_media_play;
        } else if (mState.equals(State.Retrieving)) {
            notificationAction = android.R.drawable.ic_media_play;
        } else if (mState.equals(State.Preparing)) {
            notificationAction = android.R.drawable.ic_media_pause;
        }

        largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.avatar); //replace with your own image

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //Do something
                if (notificationBuilder != null)
                    notificationBuilder.setLargeIcon(bitmap);
                else
                    largeIcon = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Intent playPauseIntent = new Intent(this, StationPlayerService.class);
        // Create a new Notification
        if (mModel != null) {
            mPicasso.load(mModel.getStationLogo())
                    .into(target);

            playPauseIntent.setAction(Constants.ACTION.PLAY_ACTION);
            playPauseIntent.putExtra("MODEL_ID", mModel.getStationID());
            playPauseIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent playPauseAction = PendingIntent.getService(this, 0,
                    playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder = new NotificationCompat.Builder(this, mModel.getStationName())
                    .setShowWhen(false)
                    // Set the Notification style
                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                            // Attach our MediaSession token
                            .setMediaSession(mediaSession.getSessionToken())
                            // Show our playback controls in the compact notification view.
                            .setShowCancelButton(true)
                            .setShowActionsInCompactView(0))
                    // Set the Notification color
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    // Set the large and small icons
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.logo)
                    // Set Notification content information
                    .setContentText(getString(R.string.title_now) + getString(R.string.title_current_program))
                    .setContentTitle(mModel.getStationName())
                    .setContentInfo(mModel.getCategoryName())
                    // Add playback actions
                    .addAction(notificationAction, "pause", playPauseAction);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(Constants.ACTION.PLAY_ACTION)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(Constants.ACTION.PAUSE_ACTION)) {
            transportControls.pause();
        }
    }
}
