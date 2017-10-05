package com.ubn.ummelquwain.models.response.program;

import com.ubn.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 9/6/2017.
 */

public class ProgramModel {
    @SerializedName("result")
    @Expose
    private ProgramResultModel result = null;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public ProgramResultModel getResult() {
        return result;
    }

    public void setResult(ProgramResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
