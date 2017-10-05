package com.ubn.ummelquwain.models.response.Sponsors;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class SponsorModel {
    @SerializedName("result")
    @Expose
    private List<SponsorResultModel> result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public List<SponsorResultModel> getResult() {
        return result;
    }

    public void setResult(List<SponsorResultModel> result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
