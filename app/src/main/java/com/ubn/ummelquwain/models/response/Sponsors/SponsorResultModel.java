package com.ubn.ummelquwain.models.response.Sponsors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class SponsorResultModel extends RealmObject{

    @PrimaryKey
    @SerializedName("SponserID")
    @Expose
    private Integer sponserID;
    @SerializedName("SponserName")
    @Expose
    private String sponserName;
    @SerializedName("SponserImage")
    @Expose
    private String sponserImage;
    @SerializedName("language")
    @Expose
    private Integer language;

    public Integer getSponserID() {
        return sponserID;
    }

    public void setSponserID(Integer sponserID) {
        this.sponserID = sponserID;
    }

    public String getSponserName() {
        return sponserName;
    }

    public void setSponserName(String sponserName) {
        this.sponserName = sponserName;
    }

    public String getSponserImage() {
        return sponserImage;
    }

    public void setSponserImage(String sponserImage) {
        this.sponserImage = sponserImage;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
