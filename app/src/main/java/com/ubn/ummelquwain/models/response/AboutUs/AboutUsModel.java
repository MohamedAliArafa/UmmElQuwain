package com.ubn.ummelquwain.models.response.AboutUs;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public class AboutUsModel {
    @SerializedName("result")
    @Expose
    private AboutUsResultModel result;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public AboutUsResultModel getResult() {
        return result;
    }

    public void setResult(AboutUsResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
