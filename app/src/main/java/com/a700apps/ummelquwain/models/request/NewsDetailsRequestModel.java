package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsDetailsRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("NewsID")
    @Expose
    private Integer newsID;

    public NewsDetailsRequestModel(Integer language, Integer newsID) {
        this.language = language;
        this.newsID = newsID;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

}
