package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class BaseRequest {

    @Expose
    @SerializedName("app")
    private int App;

    public int getApp() {
        return App;
    }

    public void setApp(int app) {
        App = app;
    }
}
