package com.ubn.ummelquwain.models.response.Station.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ScheduleDetailModel extends RealmObject{
    @SerializedName("DayName")
    @Expose
    private String dayName;
    @SerializedName("ProgramName")
    @Expose
    private String programName;
    @SerializedName("ProgramTime")
    @Expose
    private String programTime;
    @SerializedName("ProgramEndTime")
    @Expose
    private String programEndTime;
    @SerializedName("ProgramEndTimeRepeat")
    @Expose
    private String programEndTimeRepeat;
    @SerializedName("ProgramTimeRepeat")
    @Expose
    private String programTimeRepeat;
    @SerializedName("language")
    @Expose
    private Integer language;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramTime() {
        return programTime;
    }

    public void setProgramTime(String programTime) {
        this.programTime = programTime;
    }

    public String getProgramEndTime() {
        return programEndTime;
    }

    public void setProgramEndTime(String programEndTime) {
        this.programEndTime = programEndTime;
    }

    public String getProgramEndTimeRepeat() {
        return programEndTimeRepeat;
    }

    public void setProgramEndTimeRepeat(String programEndTimeRepeat) {
        this.programEndTimeRepeat = programEndTimeRepeat;
    }

    public String getProgramTimeRepeat() {
        return programTimeRepeat;
    }

    public void setProgramTimeRepeat(String programTimeRepeat) {
        this.programTimeRepeat = programTimeRepeat;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
