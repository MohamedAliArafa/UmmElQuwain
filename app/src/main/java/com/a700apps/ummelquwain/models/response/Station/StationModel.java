package com.a700apps.ummelquwain.models.response.Station;

import com.a700apps.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/5/2017.
 */

public class StationModel {
    @SerializedName("result")
    @Expose
    private StationResultModel result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public StationResultModel getResult() {
        return result;
    }

    public void setResult(StationResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
