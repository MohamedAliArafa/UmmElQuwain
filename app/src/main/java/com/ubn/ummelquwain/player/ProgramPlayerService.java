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
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.utilities.Constants;
import com.ubn.ummelquwain.utilities.Constants.State;

import io.realm.Realm;

import static android.content.Intent.ACTION_MEDIA_BUTTON;
import static com.ubn.ummelquwain.utilities.Constants.MODEL_ID;

/*
 * Created by mohamed.arafa on 8/29/2017.
 */

public class ProgramPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, Playback.Callback,
        HeadsetActionButtonReceiver.Delegate {

    private final String LOG_TAG = ProgramPlayerService.class.getName();

    //Current Playing Station
    private static ProgramResultModel mModel;

    //Instance
    private static ProgramPlayerService mInstance = null;
    private IBinder mBinder = new ProgramPlayerService.ServiceBinder();

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
    private boolean isHandsetRegistered = false;

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
//        mPlayback.start();
    }

    @Override
    public void onDestroy() {
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(State.Stopped);
        mRealm.commitTransaction();
        super.onDestroy();
    }

    public void preparePlayer(ProgramResultModel model) {
        if (mModel != null)
            if (mModel.getProgramID() != model.getProgramID()) {
                mRealm.beginTransaction();
                mModel.setPlaying(State.Stopped);
                mRealm.commitTransaction();
                mModel = model;
                mPlayback.setMedia(mModel.getProgramID(), mModel.getAudioProgramLink());
                mPlayback.play();
                transportControls.play();
                return;
            }
        mModel = model;
        if (model.isPlaying() != State.Playing && model.isPlaying() != State.Buffering) {
            mModel = model;
            startMusic();
            transportControls.play();
            isPreparing = true;
        } else {
            pauseMusic();
            transportControls.pause();
            mRealm.beginTransaction();
            if (mModel != null)
                mModel.setPlaying(State.Paused);
            mRealm.commitTransaction();
        }
    }

    public void pauseMusic() {
        if (mState.equals(State.Playing)) {
            mPlayback.setMedia(mModel.getStationID(), mModel.getAudioProgramLink());
            mPlayback.pause();
            mState = State.Paused;
        } else if (mState.equals(State.Preparing) || mState.equals(State.Retrieving)) {
            mPlayback.stop(true);
            mState = State.Retrieving;
        }
    }

    public void startMusic() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            mPlayback.setMedia(mModel.getProgramID(), mModel.getAudioProgramLink());
            mPlayback.play();
        } else if (mState.equals(State.Retrieving)) {
            mPlayback.setMedia(mModel.getProgramID(), mModel.getAudioProgramLink());
            mPlayback.play();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isHandsetRegistered)
            HeadsetActionButtonReceiver.register(getApplicationContext(), this);
        isHandsetRegistered = true;
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_STARTFOREGROUND_ACTION));
        } else if (intent.getAction().equals(ACTION_MEDIA_BUTTON)) {
            Log.i(LOG_TAG, getString(R.string.toast_media_clicked));
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_previous));
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            int id = intent.getIntExtra(MODEL_ID, 0);
            ProgramResultModel model = mRealm.where(ProgramResultModel.class).equalTo("programID", id).findFirst();
            if (model != null) preparePlayer(model);
        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) {
            pauseMusic();
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_clicked_next));
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, getString(R.string.toast_foreground_recived));
            HeadsetActionButtonReceiver.unregister();
            isHandsetRegistered = false;
            mPlayback.stop(true);
            stopForeground(true);
            stopSelf();
        }
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
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

    @Override
    public void onMediaButtonSingleClick() {
        if (mModel != null)
            preparePlayer(mModel);
    }

    @Override
    public void onMediaButtonDoubleClick() {

    }

    public class ServiceBinder extends Binder {
        public ProgramPlayerService getService() {
            return ProgramPlayerService.this;
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
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mModel.getBroadcasterName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mModel.getCategorName())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mModel.getProgramName())
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

        Intent playPauseIntent = new Intent(this, ProgramPlayerService.class);
        // Create a new Notification
        if (mModel != null) {
            mPicasso.load(mModel.getProgramLogo())
                    .into(target);

            playPauseIntent.setAction(Constants.ACTION.PLAY_ACTION);
            playPauseIntent.putExtra(MODEL_ID, mModel.getProgramID());
            playPauseIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent playPauseAction = PendingIntent.getService(this, 0,
                    playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder = new NotificationCompat.Builder(this, mModel.getProgramName())
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
                    .setContentText(getString(R.string.title_now) + mModel.getBroadcasterName())
                    .setContentTitle(mModel.getProgramName())
                    .setContentInfo(mModel.getCategorName())
                    // Add playback actions
                    .addAction(notificationAction, "pause", playPauseAction);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {}

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        isPreparing = false;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(State.Stopped);
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
            mModel.setPlaying(State.Playing);
        mRealm.commitTransaction();
        Log.i("Prepared", "true");
    }

    @Override
    public void onCompletion() {
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(State.Stopped);
        mRealm.commitTransaction();
    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        switch (state) {
            case PlaybackState.STATE_PLAYING:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(State.Playing);
                mRealm.commitTransaction();
                mState = State.Playing;
                break;
            case PlaybackState.STATE_PAUSED:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(State.Paused);
                mRealm.commitTransaction();
                mState = State.Paused;
                break;
            case PlaybackState.STATE_STOPPED:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(State.Stopped);
                mRealm.commitTransaction();
                mState = State.Stopped;
                break;
            case PlaybackState.STATE_BUFFERING:
                mRealm.beginTransaction();
                if (mModel != null)
                    mModel.setPlaying(State.Buffering);
                mRealm.commitTransaction();
                mState = State.Preparing;
                break;

        }
    }

    @Override
    public void onError(String error) {
        mState = State.Retrieving;
        mRealm.beginTransaction();
        if (mModel != null)
            mModel.setPlaying(State.Stopped);
        mRealm.commitTransaction();
    }
}
