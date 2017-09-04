package com.a700apps.ummelquwain.models.response.AboutUs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class AboutUsResultModel {
    @SerializedName("AboutUsID")
    @Expose
    private Integer aboutUsID;
    @SerializedName("MangerName")
    @Expose
    private String mangerName;
    @SerializedName("Mission")
    @Expose
    private String mission;
    @SerializedName("Vision")
    @Expose
    private String vision;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("UBNManager_Image")
    @Expose
    private Object uBNManagerImage;
    @SerializedName("UBNManager_Word")
    @Expose
    private String uBNManagerWord;

    public Integer getAboutUsID() {
        return aboutUsID;
    }

    public void setAboutUsID(Integer aboutUsID) {
        this.aboutUsID = aboutUsID;
    }

    public String getMangerName() {
        return mangerName;
    }

    public void setMangerName(String mangerName) {
        this.mangerName = mangerName;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Object getUBNManagerImage() {
        return uBNManagerImage;
    }

    public void setUBNManagerImage(Object uBNManagerImage) {
        this.uBNManagerImage = uBNManagerImage;
    }

    public String getUBNManagerWord() {
        return uBNManagerWord;
    }

    public void setUBNManagerWord(String uBNManagerWord) {
        this.uBNManagerWord = uBNManagerWord;
    }
}
