package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class AlbumContentRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;

    @SerializedName("AlbumID")
    @Expose
    private Integer AlbumID;

    @SerializedName("MediaType")
    @Expose
    private Integer MediaType;


    public Integer getAlbumID() {
        return AlbumID;
    }

    public void setAlbumID(Integer albumID) {
        AlbumID = albumID;
    }

    public Integer getMediaType() {
        return MediaType;
    }

    public void setMediaType(Integer mediaType) {
        MediaType = mediaType;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public AlbumContentRequestModel(Integer language, Integer albumID, Integer mediaType) {
        this.language = language;
        AlbumID = albumID;
        MediaType = mediaType;
    }
}
