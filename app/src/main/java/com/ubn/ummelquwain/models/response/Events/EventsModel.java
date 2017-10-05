package com.ubn.ummelquwain.models.response.Events;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventsModel {
    @SerializedName("result")
    @Expose
    private List<EventResultModel> result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public List<EventResultModel> getResult() {
        return result;
    }

    public void setResult(List<EventResultModel> result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
