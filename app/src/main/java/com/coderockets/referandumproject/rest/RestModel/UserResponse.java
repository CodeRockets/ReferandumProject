package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by aykutasil on 1.05.2016.
 */
public class UserResponse extends BaseResponse {

    @Expose
    @SerializedName("data")
    private ModelUser Data;

    public ModelUser getData() {
        return Data;
    }

    public void setData(ModelUser data) {
        Data = data;
    }
}
