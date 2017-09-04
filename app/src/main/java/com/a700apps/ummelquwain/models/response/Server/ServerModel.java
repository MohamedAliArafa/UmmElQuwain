package com.a700apps.ummelquwain.models.response.Server;

import com.a700apps.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class ServerModel {
    @SerializedName("result")
    @Expose
    private ServerResponseModel result;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public ServerResponseModel getResult() {
        return result;
    }

    public void setResult(ServerResponseModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
