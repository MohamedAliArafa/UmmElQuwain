package com.a700apps.ummelquwain.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.a700apps.ummelquwain.models.response.Station.Schedule.ScheduleModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramScheduleResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramUserCommentResultModel;
import com.a700apps.ummelquwain.player.StationPlayerService;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context, int itemWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / itemWidth);
    }

    public static int dpToPixle(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()
        );
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        final String docId = DocumentsContract.getDocumentId(contentURI);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);

        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = 0;
            if ("image".equals(type)) {
                idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            } else if ("video".equals(type)) {
                idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            } else if ("audio".equals(type)) {
                idx = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            }
            return cursor.getString(idx);
        }
    }

    public static void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity, Permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{Permission}, Code);
        }
    }

    public static boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isEventInCal(Context context, String meetingName) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.calendar/events"),
                new String[]{CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,},
                CalendarContract.Events.TITLE + " = ? ",
                new String[]{meetingName}, null);
        DatabaseUtils.dumpCursor(cursor);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public interface addStationCallback {
        void finished();
    }

    public static boolean isPlayerRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StationPlayerService.class.getName() == service.service.getClassName()) {
                return true;
            }
        }
        return false;
    }

    public static void addStationsToRealm(List<StationResultModel> mStations, addStationCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        for (StationResultModel mStation : mStations) {
            realm.executeTransaction(realm1 -> {
                StationResultModel station = realm1.where(StationResultModel.class)
                        .equalTo("stationID", mStation.getStationID()).findFirst();
                if (station == null) {
                    station = realm1.createObject(StationResultModel.class, mStation.getStationID());
                    station.setPlaying(false);
                }
                station.setStationName(mStation.getStationName());
                station.setCategoryName(mStation.getCategoryName());
                station.setStationInfo(mStation.getStationInfo());
                station.setStationWebsite(mStation.getStationWebsite());
                station.setStationLogo(mStation.getStationLogo());
                station.setStationImage(mStation.getStationImage());
                station.setStationFrequency(mStation.getStationFrequency());
                station.setLive(mStation.getLive());
                station.setVideo(mStation.getVideo());
                station.setStreamLink(mStation.getStreamLink());
                station.setWhiteLabelURL(mStation.getWhiteLabelURL());
                station.setURLPLS(mStation.getURLPLS());
                station.setVideoLink(mStation.getVideoLink());
                station.setFacebookLink(mStation.getFacebookLink());
                station.setTwitterLink(mStation.getTwitterLink());
                station.setCurrentProgramName(mStation.getCurrentProgramName());
                station.setIsFavourite(mStation.getIsFavourite());
                station.setStationLanguage(mStation.getStationLanguage());
                station.setLanguage(mStation.getLanguage());
                station.setUserID(mStation.getUserID());
                station.setKeyword(mStation.getKeyword());
                RealmList<ProgramResultModel> listPrograms = mStation.getPrograms();
                if (listPrograms != null)
                    if (!listPrograms.isManaged()) { // if the 'list' is managed, all items in it is also managed
                        RealmList<ProgramResultModel> managedImageList = new RealmList<>();
                        for (ProgramResultModel item : listPrograms) {
                            if (item.isManaged()) {
                                managedImageList.add(item);
                            } else {
                                managedImageList.add(realm.copyToRealmOrUpdate(item));
                            }
                        }
                        listPrograms = managedImageList;
                    }
                station.setPrograms(listPrograms);
                RealmList<ScheduleModel> listSchedule = mStation.getSchedule();
                if (listSchedule != null)
                    if (!listSchedule.isManaged()) { // if the 'list' is managed, all items in it is also managed
                        RealmList<ScheduleModel> managedImageList = new RealmList<>();
                        for (ScheduleModel item : listSchedule) {
                            if (item.isManaged()) {
                                managedImageList.add(item);
                            } else {
                                managedImageList.add(realm.copyToRealmOrUpdate(item));
                            }
                        }
                        listSchedule = managedImageList;
                    }
                station.setSchedule(listSchedule);

                realm1.copyToRealmOrUpdate(station);
            });
        }
        realm.close();
        callback.finished();
    }

    public static void addStationToRealm(StationResultModel mStation, addStationCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            StationResultModel station = realm1.where(StationResultModel.class)
                    .equalTo("stationID", mStation.getStationID()).findFirst();
            if (station == null) {
                station = realm1.createObject(StationResultModel.class, mStation.getStationID());
                station.setPlaying(false);
            }
            station.setStationName(mStation.getStationName());
            station.setCategoryName(mStation.getCategoryName());
            station.setStationInfo(mStation.getStationInfo());
            station.setStationWebsite(mStation.getStationWebsite());
            station.setStationLogo(mStation.getStationLogo());
            station.setStationImage(mStation.getStationImage());
            station.setStationFrequency(mStation.getStationFrequency());
            station.setLive(mStation.getLive());
            station.setVideo(mStation.getVideo());
            station.setStreamLink(mStation.getStreamLink());
            station.setWhiteLabelURL(mStation.getWhiteLabelURL());
            station.setURLPLS(mStation.getURLPLS());
            station.setVideoLink(mStation.getVideoLink());
            station.setFacebookLink(mStation.getFacebookLink());
            station.setTwitterLink(mStation.getTwitterLink());
            station.setCurrentProgramName(mStation.getCurrentProgramName());
            station.setIsFavourite(mStation.getIsFavourite());
            station.setStationLanguage(mStation.getStationLanguage());
            station.setLanguage(mStation.getLanguage());
            station.setUserID(mStation.getUserID());
            station.setKeyword(mStation.getKeyword());
            RealmList<ProgramResultModel> listComments = mStation.getPrograms();
            if (listComments != null)
                if (!listComments.isManaged()) { // if the 'list' is managed, all items in it is also managed
                    RealmList<ProgramResultModel> managedImageList = new RealmList<>();
                    for (ProgramResultModel item : listComments) {
                        if (item.isManaged()) {
                            managedImageList.add(item);
                        } else {
                            managedImageList.add(realm.copyToRealmOrUpdate(item));
                        }
                    }
                    listComments = managedImageList;
                }
            station.setPrograms(listComments);
            RealmList<ScheduleModel> listSchedule = mStation.getSchedule();
            if (listSchedule != null)
                if (!listSchedule.isManaged()) { // if the 'list' is managed, all items in it is also managed
                    RealmList<ScheduleModel> managedImageList = new RealmList<>();
                    for (ScheduleModel item : listSchedule) {
                        if (item.isManaged()) {
                            managedImageList.add(item);
                        } else {
                            managedImageList.add(realm.copyToRealmOrUpdate(item));
                        }
                    }
                    listSchedule = managedImageList;
                }
            station.setSchedule(listSchedule);
            realm1.copyToRealmOrUpdate(station);
        });
        realm.close();
        callback.finished();
    }

    public static void addProgramsToRealm(List<ProgramResultModel> program, addStationCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        for (ProgramResultModel mProgram : program) {
            realm.executeTransaction(realm1 -> {
                ProgramResultModel Program = realm1.where(ProgramResultModel.class)
                        .equalTo("programID", mProgram.getProgramID()).findFirst();
                if (Program == null) {
                    Program = realm1.createObject(ProgramResultModel.class, mProgram.getProgramID());
                    Program.setPlaying(false);
                }
                Program.setStationID(mProgram.getStationID());
                Program.setCategorName(mProgram.getCategorName());
                Program.setProgramName(mProgram.getProgramName());
                Program.setProgramDescription(mProgram.getProgramDescription());
                Program.setProgramInfo(mProgram.getProgramInfo());
                Program.setProgramLogo(mProgram.getProgramLogo());
                Program.setIsLiveAudio(mProgram.getIsLiveAudio());
                Program.setAudioProgramLink(mProgram.getAudioProgramLink());
                Program.setIsLiveVideo(mProgram.getIsLiveVideo());
                Program.setVedioProgramLink(mProgram.getVedioProgramLink());
                Program.setProgramImage(mProgram.getProgramImage());
                Program.setProgramTypeName(mProgram.getProgramTypeName());
                Program.setBroadcasterName(mProgram.getBroadcasterName());
                Program.setIsFavourite(mProgram.getIsFavourite());
                Program.setUserID(mProgram.getUserID());
                Program.setKeyword(mProgram.getKeyword());
                Program.setLanguage(mProgram.getLanguage());
                RealmList<ProgramUserCommentResultModel> listComments = mProgram.getUserComments();
                if (listComments != null)
                    if (!listComments.isManaged()) { // if the 'list' is managed, all items in it is also managed
                        RealmList<ProgramUserCommentResultModel> managedImageList = new RealmList<>();
                        for (ProgramUserCommentResultModel item : listComments) {
                            if (item.isManaged()) {
                                managedImageList.add(item);
                            } else {
                                managedImageList.add(realm.copyToRealmOrUpdate(item));
                            }
                        }
                        listComments = managedImageList;
                    }
                Program.setUserComments(listComments);
                RealmList<ProgramScheduleResultModel> listSchedule = mProgram.getSchedule();
                if (listSchedule != null)
                    if (!listSchedule.isManaged()) { // if the 'list' is managed, all items in it is also managed
                        RealmList<ProgramScheduleResultModel> managedImageList = new RealmList<>();
                        for (ProgramScheduleResultModel item : listSchedule) {
                            if (item.isManaged()) {
                                managedImageList.add(item);
                            } else {
                                managedImageList.add(realm.copyToRealmOrUpdate(item));
                            }
                        }
                        listSchedule = managedImageList;
                    }
                Program.setSchedule(listSchedule);

                realm1.copyToRealmOrUpdate(Program);
            });
        }
        realm.close();
        callback.finished();
    }

    public static void addProgramToRealm(ProgramResultModel mProgram, addStationCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            ProgramResultModel Program = realm1.where(ProgramResultModel.class)
                    .equalTo("programID", mProgram.getProgramID()).findFirst();
            if (Program == null) {
                Program = realm1.createObject(ProgramResultModel.class, mProgram.getProgramID());
                Program.setPlaying(false);
            }
            Program.setStationID(mProgram.getStationID());
            Program.setCategorName(mProgram.getCategorName());
            Program.setProgramName(mProgram.getProgramName());
            Program.setProgramDescription(mProgram.getProgramDescription());
            Program.setProgramInfo(mProgram.getProgramInfo());
            Program.setProgramLogo(mProgram.getProgramLogo());
            Program.setIsLiveAudio(mProgram.getIsLiveAudio());
            Program.setAudioProgramLink(mProgram.getAudioProgramLink());
            Program.setIsLiveVideo(mProgram.getIsLiveVideo());
            Program.setVedioProgramLink(mProgram.getVedioProgramLink());
            Program.setProgramImage(mProgram.getProgramImage());
            Program.setProgramTypeName(mProgram.getProgramTypeName());
            Program.setBroadcasterName(mProgram.getBroadcasterName());
            Program.setIsFavourite(mProgram.getIsFavourite());
            Program.setUserID(mProgram.getUserID());
            Program.setKeyword(mProgram.getKeyword());
            Program.setLanguage(mProgram.getLanguage());
            RealmList<ProgramUserCommentResultModel> listComments = mProgram.getUserComments();
            if (listComments != null)
                if (!listComments.isManaged()) { // if the 'list' is managed, all items in it is also managed
                    RealmList<ProgramUserCommentResultModel> managedImageList = new RealmList<>();
                    for (ProgramUserCommentResultModel item : listComments) {
                        if (item.isManaged()) {
                            managedImageList.add(item);
                        } else {
                            managedImageList.add(realm.copyToRealmOrUpdate(item));
                        }
                    }
                    listComments = managedImageList;
                }
            Program.setUserComments(listComments);
            RealmList<ProgramScheduleResultModel> listSchedule = mProgram.getSchedule();
            if (listSchedule != null)
                if (!listSchedule.isManaged()) { // if the 'list' is managed, all items in it is also managed
                    RealmList<ProgramScheduleResultModel> managedImageList = new RealmList<>();
                    for (ProgramScheduleResultModel item : listSchedule) {
                        if (item.isManaged()) {
                            managedImageList.add(item);
                        } else {
                            managedImageList.add(realm.copyToRealmOrUpdate(item));
                        }
                    }
                    listSchedule = managedImageList;
                }
            Program.setSchedule(listSchedule);

            realm1.copyToRealmOrUpdate(Program);
        });

        realm.close();
        callback.finished();
    }
}
