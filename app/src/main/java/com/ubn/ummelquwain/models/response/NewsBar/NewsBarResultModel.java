package com.ubn.ummelquwain.models.response.NewsBar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsBarResultModel {
    @SerializedName("NewsID")
    @Expose
    private Integer newsID;
    @SerializedName("NewsTitle")
    @Expose
    private String newsTitle;
    @SerializedName("NewsDescription")
    @Expose
    private String newsDescription;
    @SerializedName("NewsImage")
    @Expose
    private String newsImage;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("NewsDate")
    @Expose
    private String newsDate;
    @SerializedName("NewsTime")
    @Expose
    private String newsTime;
    @SerializedName("NewsName_Type")
    @Expose
    private Object newsNameType;

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public Object getNewsNameType() {
        return newsNameType;
    }

    public void setNewsNameType(Object newsNameType) {
        this.newsNameType = newsNameType;
    }
}
