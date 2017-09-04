package com.a700apps.ummelquwain.models.response.Events;

import com.a700apps.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventDetailModel {
    @SerializedName("result")
    @Expose
    private EventResultModel result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public EventResultModel getResult() {
        return result;
    }

    public void setResult(EventResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
