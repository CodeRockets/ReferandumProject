package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelAnswer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by aykutasil on 1.05.2016.
 */
public class AnswerResponse extends BaseResponse {

    @Expose
    @SerializedName("data")
    private ModelAnswer Data;

    public ModelAnswer getData() {
        return Data;
    }

    public void setData(ModelAnswer data) {
        Data = data;
    }
}
