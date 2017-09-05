package com.a700apps.ummelquwain.models.response.Albums;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class MediaResultModel extends RealmObject implements Parcelable{

    @PrimaryKey
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mediaID);
        dest.writeValue(this.albumID);
        dest.writeValue(this.mediaType);
        dest.writeString(this.attachmentURL);
        dest.writeValue(this.isYoutube);
        dest.writeString(this.videoThumb);
        dest.writeString(this.description);
        dest.writeValue(this.language);
    }

    public MediaResultModel() {
    }

    protected MediaResultModel(Parcel in) {
        this.mediaID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.albumID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mediaType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attachmentURL = in.readString();
        this.isYoutube = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.videoThumb = in.readString();
        this.description = in.readString();
        this.language = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<MediaResultModel> CREATOR = new Creator<MediaResultModel>() {
        @Override
        public MediaResultModel createFromParcel(Parcel source) {
            return new MediaResultModel(source);
        }

        @Override
        public MediaResultModel[] newArray(int size) {
            return new MediaResultModel[size];
        }
    };
}
