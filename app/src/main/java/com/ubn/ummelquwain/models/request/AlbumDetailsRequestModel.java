package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class AlbumDetailsRequestModel {
    @SerializedName("AlbumID")
    @Expose
    private Integer albumID;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("MediaType")
    @Expose
    private Integer mediaType;

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }
}
