package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class SoruSorResponse extends BaseResponse {

    @Expose
    @SerializedName("data")
    private ModelQuestionInformation Data;

    public ModelQuestionInformation getData() {
        return Data;
    }

    public void setData(ModelQuestionInformation data) {
        Data = data;
    }
}
