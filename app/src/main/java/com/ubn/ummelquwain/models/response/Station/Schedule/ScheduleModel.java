package com.ubn.ummelquwain.models.response.Station.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ScheduleModel extends RealmObject {

    @PrimaryKey
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("DayName")
    @Expose
    private String dayName;
    @SerializedName("Schedule")
    @Expose
    private RealmList<ScheduleDetailModel> schedule = null;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public RealmList<ScheduleDetailModel> getSchedule() {
        return schedule;
    }

    public void setSchedule(RealmList<ScheduleDetailModel> schedule) {
        this.schedule = schedule;
    }
}
