package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 25.04.2016.
 */
public class SoruGetirBaseResponse {
    @SerializedName("data")
    private SoruGetirResponse Data;

    public SoruGetirResponse getData() {
        return Data;
    }

    public void setData(SoruGetirResponse data) {
        Data = data;
    }
}
