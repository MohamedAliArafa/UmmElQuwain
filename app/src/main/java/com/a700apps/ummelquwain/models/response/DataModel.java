package com.a700apps.ummelquwain.models.response;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataModel {

    @SerializedName("ResultData")
    @Expose
    private Integer resultData;

    public Integer getResultData() {
        return resultData;
    }

    public void setResultData(Integer resultData) {
        this.resultData = resultData;
    }

}