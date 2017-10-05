package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class StationsRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("UserID")
    @Expose
    private String userID;

    public StationsRequestModel(int language, String userID) {
        this.language= language;
        this.userID = userID;
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
}
