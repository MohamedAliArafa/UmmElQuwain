package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public class SearchRequestModel {

    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("language")
    @Expose
    private Integer language;

    public SearchRequestModel(String keyword, String userID, Integer language) {
        this.keyword = keyword;
        this.userID = userID;
        this.language = language;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
