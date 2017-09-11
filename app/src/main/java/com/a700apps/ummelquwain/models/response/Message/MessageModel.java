package com.a700apps.ummelquwain.models.response.Message;

import com.a700apps.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Created by mohamed.arafa on 9/11/2017.
 */

public class MessageModel {
    @SerializedName("result")
    @Expose
    private MessageResultModel result;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public MessageResultModel getResult() {
        return result;
    }

    public void setResult(MessageResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
