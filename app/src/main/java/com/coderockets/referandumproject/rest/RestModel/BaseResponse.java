package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class BaseResponse {

    @Expose
    @SerializedName("statusCode")
    private int StatusCode;

    @Expose
    @SerializedName("error")
    private String Error;

    @Expose
    @SerializedName("message")
    private String Message;

    @Expose
    @SerializedName("timestamp")
    private long TimeStamp;


    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
