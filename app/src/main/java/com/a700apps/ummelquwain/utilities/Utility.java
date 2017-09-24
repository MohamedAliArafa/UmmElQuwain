package com.a700apps.ummelquwain.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;

import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context, int itemWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / itemWidth);
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
                CalendarContract.Events.TITLE +" = ? ",
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

    public interface addStationCallback{
        void finished();
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
                station.setPrograms(mStation.getPrograms());
                station.setSchedule(mStation.getSchedule());

                realm1.copyToRealmOrUpdate(station);
            });
        }
        realm.close();
        callback.finished();
    }

    public static void addProgramsToRealm(List<ProgramResultModel> mStations, addStationCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        for (ProgramResultModel mProgram : mStations) {
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
                Program.setUserComments(mProgram.getUserComments());
                Program.setSchedule(mProgram.getSchedule());

                realm1.copyToRealmOrUpdate(Program);
            });
        }
        realm.close();
        callback.finished();
    }
}
