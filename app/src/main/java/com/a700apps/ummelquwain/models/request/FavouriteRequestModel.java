package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public class FavouriteRequestModel {
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("FavouriteType")
    @Expose
    private Integer favouriteType;
    @SerializedName("ItemID")
    @Expose
    private Integer itemID;

    public FavouriteRequestModel(String userID,String deviceID, Integer favouriteType, Integer itemID) {
        this.deviceID = deviceID;
        this.userID = userID;
        this.favouriteType = favouriteType;
        this.itemID = itemID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getFavouriteType() {
        return favouriteType;
    }

    public void setFavouriteType(Integer favouriteType) {
        this.favouriteType = favouriteType;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }
}
