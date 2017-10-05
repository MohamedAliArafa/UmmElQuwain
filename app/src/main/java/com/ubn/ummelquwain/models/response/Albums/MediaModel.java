package com.ubn.ummelquwain.models.response.Albums;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class MediaModel {

    @SerializedName("result")
    @Expose
    private MediaResultModel result;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public MediaResultModel getResult() {
        return result;
    }

    public void setResult(MediaResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }


}
