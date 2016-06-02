package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class BaseRequest {

    @SerializedName("app")
    private String App = "0";

    public String getApp() {
        return App;
    }

    public void setApp(String app) {
        this.App = app;
    }
}
