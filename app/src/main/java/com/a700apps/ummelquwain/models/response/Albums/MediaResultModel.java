package com.a700apps.ummelquwain.models.response.Albums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class MediaResultModel {
    @SerializedName("MediaID")
    @Expose
    private Integer mediaID;
    @SerializedName("AlbumID")
    @Expose
    private Integer albumID;
    @SerializedName("MediaType")
    @Expose
    private Integer mediaType;
    @SerializedName("AttachmentURL")
    @Expose
    private String attachmentURL;
    @SerializedName("IsYoutube")
    @Expose
    private Boolean isYoutube;
    @SerializedName("VideoThumb")
    @Expose
    private String videoThumb;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("language")
    @Expose
    private Integer language;

    public Integer getMediaID() {
        return mediaID;
    }

    public void setMediaID(Integer mediaID) {
        this.mediaID = mediaID;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public String getAttachmentURL() {
        return attachmentURL;
    }

    public void setAttachmentURL(String attachmentURL) {
        this.attachmentURL = attachmentURL;
    }

    public Boolean getIsYoutube() {
        return isYoutube;
    }

    public void setIsYoutube(Boolean isYoutube) {
        this.isYoutube = isYoutube;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
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
}
