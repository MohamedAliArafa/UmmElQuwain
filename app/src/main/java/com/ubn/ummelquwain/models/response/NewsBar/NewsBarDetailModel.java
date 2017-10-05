package com.ubn.ummelquwain.models.response.NewsBar;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsBarDetailModel {
    @SerializedName("result")
    @Expose
    private NewsBarResultModel result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public NewsBarResultModel getResult() {
        return result;
    }

    public void setResult(NewsBarResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
