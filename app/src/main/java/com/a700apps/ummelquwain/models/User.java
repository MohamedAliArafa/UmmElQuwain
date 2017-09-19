package com.a700apps.ummelquwain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public class User extends RealmObject{

    @PrimaryKey
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("SocialMediaID")
    @Expose
    private String socialMediaID;
    @SerializedName("SocialMediaType")
    @Expose
    private Integer socialMediaType;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;

    public String getID() {
        return id;
    }

    public void setID(String iD) {
        this.id = iD;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSocialMediaID() {
        return socialMediaID;
    }

    public void setSocialMediaID(String socialMediaID) {
        this.socialMediaID = socialMediaID;
    }

    public Integer getSocialMediaType() {
        return socialMediaType;
    }

    public void setSocialMediaType(Integer socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
