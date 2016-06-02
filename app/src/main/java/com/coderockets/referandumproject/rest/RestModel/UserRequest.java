package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
public class UserRequest extends BaseRequest {
    /*
    {
        "token": "string"
    }
    */

    @SerializedName("token")
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
