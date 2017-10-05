package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class LanguageRequestModel extends RealmObject{
    @PrimaryKey
    private int id;

    @SerializedName("language")
    @Expose
    private Integer language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LanguageRequestModel() {
    }

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
