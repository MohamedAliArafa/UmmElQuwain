package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class StationDetailsRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("StationID")
    @Expose
    private Integer stationID;

    public StationDetailsRequestModel(Integer language, String userID, Integer stationID) {
        this.language = language;
        this.userID = userID;
        this.stationID = stationID;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }
}
