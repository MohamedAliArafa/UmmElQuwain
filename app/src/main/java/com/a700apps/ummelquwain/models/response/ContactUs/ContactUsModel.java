package com.a700apps.ummelquwain.models.response.ContactUs;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

import com.a700apps.ummelquwain.models.response.DataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class ContactUsModel {

    @SerializedName("result")
    @Expose
    private ContactUsResultModel result;
    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public ContactUsResultModel getResult() {
        return result;
    }

    public void setResult(ContactUsResultModel result) {
        this.result = result;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

}
