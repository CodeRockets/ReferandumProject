package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelUserQuestions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 6.09.2016.
 */
public class UserQuestionsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ModelUserQuestions Data;

    public ModelUserQuestions getData() {
        return Data;
    }

    public void setData(ModelUserQuestions data) {
        Data = data;
    }
}
