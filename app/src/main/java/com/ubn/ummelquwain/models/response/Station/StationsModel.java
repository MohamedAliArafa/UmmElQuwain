package com.ubn.ummelquwain.models.response.Station;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class StationsModel {
    @SerializedName("result")
    @Expose
    private List<StationResultModel> result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public List<StationResultModel> getResult() {
        return result;
    }

    public void setResult(List<StationResultModel> result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
