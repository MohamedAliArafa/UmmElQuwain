package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class LanguageRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;

    public LanguageRequestModel(Integer language) {
        this.language = language;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
