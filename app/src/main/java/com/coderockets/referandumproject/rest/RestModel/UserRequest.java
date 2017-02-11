package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
public class UserRequest {
    /*
    {
        "token": "string"
    }
    */

    @Expose
    @SerializedName("token")
    private String Token;

    @Expose
    @SerializedName("reg_id")
    private String RegId;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRegId() {
        return RegId;
    }

    public void setRegId(String regId) {
        RegId = regId;
    }
}
