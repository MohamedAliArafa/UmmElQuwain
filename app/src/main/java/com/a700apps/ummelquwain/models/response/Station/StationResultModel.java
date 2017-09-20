package com.a700apps.ummelquwain.models.response.Station;

import android.os.Parcel;
import android.os.Parcelable;

import com.a700apps.ummelquwain.models.response.Station.Schedule.ScheduleModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class StationResultModel  extends RealmObject implements Parcelable{

    @PrimaryKey
    @SerializedName("StationID")
    @Expose
    private Integer stationID;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("StationName")
    @Expose
    private String stationName;
    @SerializedName("StationInfo")
    @Expose
    private String stationInfo;
    @SerializedName("StationWebsite")
    @Expose
    private String stationWebsite;
    @SerializedName("StationLogo")
    @Expose
    private String stationLogo;
    @SerializedName("StationImage")
    @Expose
    private String stationImage;
    @SerializedName("StationFrequency")
    @Expose
    private String stationFrequency;
    @SerializedName("IsLive")
    @Expose
    private Boolean isLive;
    @SerializedName("IsVideo")
    @Expose
    private Boolean isVideo;
    @SerializedName("StreamLink")
    @Expose
    private String streamLink;
    @SerializedName("WhiteLabelURL")
    @Expose
    private String whiteLabelURL;
    @SerializedName("URLPLS")
    @Expose
    private String uRLPLS;
    @SerializedName("VideoLink")
    @Expose
    private String videoLink;
    @SerializedName("FacebookLink")
    @Expose
    private String facebookLink;
    @SerializedName("TwitterLink")
    @Expose
    private String twitterLink;
    @SerializedName("CurrentProgramName")
    @Expose
    private String currentProgramName;
    @SerializedName("IsFavourite")
    @Expose
    private Integer isFavourite;

    private Boolean isPlaying;
    @SerializedName("StationLanguage")
    @Expose
    private String stationLanguage;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("Programs")
    @Expose
    private RealmList<ProgramResultModel> programs;
    @SerializedName("Schedule")
    @Expose
    private RealmList<ScheduleModel> schedule;

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public String getuRLPLS() {
        return uRLPLS;
    }

    public void setuRLPLS(String uRLPLS) {
        this.uRLPLS = uRLPLS;
    }

    public RealmList<ProgramResultModel> getPrograms() {
        return programs;
    }

    public void setPrograms(RealmList<ProgramResultModel> programs) {
        this.programs = programs;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(String stationInfo) {
        this.stationInfo = stationInfo;
    }

    public String getStationWebsite() {
        return stationWebsite;
    }

    public void setStationWebsite(String stationWebsite) {
        this.stationWebsite = stationWebsite;
    }

    public String getStationLogo() {
        return stationLogo;
    }

    public void setStationLogo(String stationLogo) {
        this.stationLogo = stationLogo;
    }

    public String getStationImage() {
        return stationImage;
    }

    public void setStationImage(String stationImage) {
        this.stationImage = stationImage;
    }

    public String getStationFrequency() {
        return stationFrequency;
    }

    public void setStationFrequency(String stationFrequency) {
        this.stationFrequency = stationFrequency;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getStreamLink() {
        return streamLink;
    }

    public void setStreamLink(String streamLink) {
        this.streamLink = streamLink;
    }

    public String getWhiteLabelURL() {
        return whiteLabelURL;
    }

    public void setWhiteLabelURL(String whiteLabelURL) {
        this.whiteLabelURL = whiteLabelURL;
    }

    public String getURLPLS() {
        return uRLPLS;
    }

    public void setURLPLS(String uRLPLS) {
        this.uRLPLS = uRLPLS;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getCurrentProgramName() {
        return currentProgramName;
    }

    public void setCurrentProgramName(String currentProgramName) {
        this.currentProgramName = currentProgramName;
    }

    public Integer getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Integer isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getStationLanguage() {
        return stationLanguage;
    }

    public void setStationLanguage(String stationLanguage) {
        this.stationLanguage = stationLanguage;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
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

    public RealmList<ScheduleModel> getSchedule() {
        return schedule;
    }

    public void setSchedule(RealmList<ScheduleModel> schedule) {
        this.schedule = schedule;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.stationID);
        dest.writeString(this.categoryName);
        dest.writeString(this.stationName);
        dest.writeString(this.stationInfo);
        dest.writeString(this.stationWebsite);
        dest.writeString(this.stationLogo);
        dest.writeString(this.stationImage);
        dest.writeString(this.stationFrequency);
        dest.writeValue(this.isLive);
        dest.writeValue(this.isVideo);
        dest.writeString(this.streamLink);
        dest.writeString(this.whiteLabelURL);
        dest.writeString(this.uRLPLS);
        dest.writeString(this.videoLink);
        dest.writeString(this.facebookLink);
        dest.writeString(this.twitterLink);
        dest.writeString(this.currentProgramName);
        dest.writeValue(this.isFavourite);
        dest.writeString(this.stationLanguage);
        dest.writeValue(this.language);
        dest.writeValue(this.userID);
        dest.writeString(this.keyword);
    }

    public StationResultModel() {
    }

    protected StationResultModel(Parcel in) {
        this.stationID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.categoryName = in.readString();
        this.stationName = in.readString();
        this.stationInfo = in.readString();
        this.stationWebsite = in.readString();
        this.stationLogo = in.readString();
        this.stationImage = in.readString();
        this.stationFrequency = in.readString();
        this.isLive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isVideo = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.streamLink = in.readString();
        this.whiteLabelURL = in.readString();
        this.uRLPLS = in.readString();
        this.videoLink = in.readString();
        this.facebookLink = in.readString();
        this.twitterLink = in.readString();
        this.currentProgramName = in.readString();
        this.isFavourite = (Integer) in.readValue(Integer.class.getClassLoader());
        this.stationLanguage = in.readString();
        this.language = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.keyword = in.readString();
    }

    public static final Creator<StationResultModel> CREATOR = new Creator<StationResultModel>() {
        @Override
        public StationResultModel createFromParcel(Parcel source) {
            return new StationResultModel(source);
        }

        @Override
        public StationResultModel[] newArray(int size) {
            return new StationResultModel[size];
        }
    };
}
