package com.a700apps.ummelquwain.models.response.ContactUs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*
 * Created by mohamed.arafa on 8/27/2017.
 */

public class ContactUsResultModel extends RealmObject{

    @PrimaryKey
    @SerializedName("ContactUsID")
    @Expose
    private Integer contactUsID;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("WebsiteLink")
    @Expose
    private String websiteLink;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("EMail")
    @Expose
    private String eMail;
    @SerializedName("FacebookUrl")
    @Expose
    private String facebookUrl;
    @SerializedName("TwitterUrl")
    @Expose
    private String twitterUrl;
    @SerializedName("InstagramUrl")
    @Expose
    private String instagramUrl;
    @SerializedName("Fax")
    @Expose
    private String fax;
    @SerializedName("Longtiude")
    @Expose
    private String longtiude;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("LinkedInUrl")
    @Expose
    private String linkedInUrl;
    @SerializedName("YouTubeUrl")
    @Expose
    private String youTubeUrl;

    public Integer getContactUsID() {
        return contactUsID;
    }

    public void setContactUsID(Integer contactUsID) {
        this.contactUsID = contactUsID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(String longtiude) {
        this.longtiude = longtiude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getYouTubeUrl() {
        return youTubeUrl;
    }

    public void setYouTubeUrl(String youTubeUrl) {
        this.youTubeUrl = youTubeUrl;
    }
}