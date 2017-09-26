package com.a700apps.ummelquwain.models.response.program;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramScheduleResultModel extends RealmObject{

    @PrimaryKey
    @SerializedName("ScheduleID")
    @Expose
    private Integer scheduleID;
    @SerializedName("StationID")
    @Expose
    private Integer stationID;
    @SerializedName("StationName")
    @Expose
    private String stationName;
    @SerializedName("ProgramTypeName")
    @Expose
    private String programTypeName;
    @SerializedName("ProgramTime")
    @Expose
    private String programTime;
    @SerializedName("ProgrameID")
    @Expose
    private Integer programeID;
    @SerializedName("ProgramTimeRepeat")
    @Expose
    private String programTimeRepeat;
    @SerializedName("Week_DayName")
    @Expose
    private String weekDayName;
    @SerializedName("Week_DayName_Repeat")
    @Expose
    private String weekDayNameRepeat;
    @SerializedName("language")
    @Expose
    private Integer language;

    public Integer getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(Integer scheduleID) {
        this.scheduleID = scheduleID;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getProgramTypeName() {
        return programTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        this.programTypeName = programTypeName;
    }

    public String getProgramTime() {
        return programTime;
    }

    public void setProgramTime(String programTime) {
        this.programTime = programTime;
    }

    public Integer getProgrameID() {
        return programeID;
    }

    public void setProgrameID(Integer programeID) {
        this.programeID = programeID;
    }

    public String getProgramTimeRepeat() {
        return programTimeRepeat;
    }

    public void setProgramTimeRepeat(String programTimeRepeat) {
        this.programTimeRepeat = programTimeRepeat;
    }

    public String getWeekDayName() {
        return weekDayName;
    }

    public void setWeekDayName(String weekDayName) {
        this.weekDayName = weekDayName;
    }

    public String getWeekDayNameRepeat() {
        return weekDayNameRepeat;
    }

    public void setWeekDayNameRepeat(String weekDayNameRepeat) {
        this.weekDayNameRepeat = weekDayNameRepeat;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
