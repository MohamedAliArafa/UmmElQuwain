package com.ubn.ummelquwain.models.response.NewsBar;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class NewsBarModel {
    @SerializedName("result")
    @Expose
    private List<NewsBarResultModel> result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public List<NewsBarResultModel> getResult() {
        return result;
    }

    public void setResult(List<NewsBarResultModel> result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
