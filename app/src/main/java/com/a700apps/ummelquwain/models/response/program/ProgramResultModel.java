package com.a700apps.ummelquwain.models.response.program;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/6/2017.
 */

public class ProgramResultModel extends RealmObject {

    @PrimaryKey
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("StationID")
    @Expose
    private Integer stationID;
    @SerializedName("CategorName")
    @Expose
    private String categorName;
    @SerializedName("ProgramName")
    @Expose
    private String programName;
    @SerializedName("ProgramDescription")
    @Expose
    private String programDescription;
    @SerializedName("ProgramInfo")
    @Expose
    private String programInfo;
    @SerializedName("ProgramLogo")
    @Expose
    private String programLogo;
    @SerializedName("IsLiveAudio")
    @Expose
    private Boolean isLiveAudio;
    @SerializedName("AudioProgramLink")
    @Expose
    private String audioProgramLink;
    @SerializedName("IsLiveVideo")
    @Expose
    private Boolean isLiveVideo;
    @SerializedName("VedioProgramLink")
    @Expose
    private String vedioProgramLink;
    @SerializedName("ProgramImage")
    @Expose
    private String programImage;
    @SerializedName("ProgramTypeName")
    @Expose
    private String programTypeName;
    @SerializedName("IsFavourite")
    @Expose
    private Integer isFavourite;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("language")
    @Expose
    private Integer language;

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public String getCategorName() {
        return categorName;
    }

    public void setCategorName(String categorName) {
        this.categorName = categorName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public String getProgramInfo() {
        return programInfo;
    }

    public void setProgramInfo(String programInfo) {
        this.programInfo = programInfo;
    }

    public String getProgramLogo() {
        return programLogo;
    }

    public void setProgramLogo(String programLogo) {
        this.programLogo = programLogo;
    }

    public Boolean getIsLiveAudio() {
        return isLiveAudio;
    }

    public void setIsLiveAudio(Boolean isLiveAudio) {
        this.isLiveAudio = isLiveAudio;
    }

    public String getAudioProgramLink() {
        return audioProgramLink;
    }

    public void setAudioProgramLink(String audioProgramLink) {
        this.audioProgramLink = audioProgramLink;
    }

    public Boolean getIsLiveVideo() {
        return isLiveVideo;
    }

    public void setIsLiveVideo(Boolean isLiveVideo) {
        this.isLiveVideo = isLiveVideo;
    }

    public String getVedioProgramLink() {
        return vedioProgramLink;
    }

    public void setVedioProgramLink(String vedioProgramLink) {
        this.vedioProgramLink = vedioProgramLink;
    }

    public String getProgramImage() {
        return programImage;
    }

    public void setProgramImage(String programImage) {
        this.programImage = programImage;
    }

    public String getProgramTypeName() {
        return programTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        this.programTypeName = programTypeName;
    }

    public Integer getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Integer isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}