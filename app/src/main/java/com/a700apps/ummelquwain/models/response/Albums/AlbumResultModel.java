package com.a700apps.ummelquwain.models.response.Albums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class AlbumResultModel {
    @SerializedName("AlbumID")
    @Expose
    private Integer albumID;
    @SerializedName("AlbumName")
    @Expose
    private String albumName;
    @SerializedName("AlbumImage")
    @Expose
    private String albumImage;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("keyword")
    @Expose
    private Object keyword;
    @SerializedName("MediaType")
    @Expose
    private Integer mediaType;
    @SerializedName("AlbumDescription")
    @Expose
    private String albumDescription;
    @SerializedName("LstAlbumContent")
    @Expose
    private List<MediaResultModel> lstAlbumContent = null;

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Object getKeyword() {
        return keyword;
    }

    public void setKeyword(Object keyword) {
        this.keyword = keyword;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public List<MediaResultModel> getLstAlbumContent() {
        return lstAlbumContent;
    }

    public void setLstAlbumContent(List<MediaResultModel> lstAlbumContent) {
        this.lstAlbumContent = lstAlbumContent;
    }
}
