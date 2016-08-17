package com.coderockets.referandumproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 17.08.2016.
 */
public class ModelImage {

    @Expose
    @SerializedName("img")
    private String Img;

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
