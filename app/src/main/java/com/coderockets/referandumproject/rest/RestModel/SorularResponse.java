package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by aykutasil on 20.03.2016.
 */
public class SorularResponse extends BaseResponse {

    @SerializedName("data")
    private List<ModelQuestionInformation> Data;

    public List<ModelQuestionInformation> getData() {
        return Data;
    }

    public void setData(List<ModelQuestionInformation> data) {
        Data = data;
    }
}
