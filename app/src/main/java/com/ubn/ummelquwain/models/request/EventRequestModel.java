package com.ubn.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventRequestModel {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("EventID")
    @Expose
    private Integer eventID;

    public EventRequestModel(Integer language, Integer eventID) {
        this.language = language;
        this.eventID = eventID;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }
}
