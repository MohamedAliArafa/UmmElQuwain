package com.a700apps.ummelquwain.models.response.Events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventResultModel {
    @SerializedName("EventID")
    @Expose
    private Integer eventID;
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("EventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("EventPlace")
    @Expose
    private String eventPlace;
    @SerializedName("EventImage")
    @Expose
    private String eventImage;
    @SerializedName("EventStartDate")
    @Expose
    private String eventStartDate;
    @SerializedName("EventEndDate")
    @Expose
    private String eventEndDate;
    @SerializedName("language")
    @Expose
    private Integer language;

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
}
