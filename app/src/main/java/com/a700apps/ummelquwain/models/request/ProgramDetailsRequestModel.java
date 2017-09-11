package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class ProgramDetailsRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;

    public ProgramDetailsRequestModel(Integer language, String userID, Integer programID) {
        this.language = language;
        this.userID = userID;
        this.programID = programID;
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

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }
}
