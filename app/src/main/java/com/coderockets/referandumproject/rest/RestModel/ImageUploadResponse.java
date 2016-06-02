package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
public class ImageUploadResponse extends BaseResponse {

    @SerializedName("data")
    private String Data;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
