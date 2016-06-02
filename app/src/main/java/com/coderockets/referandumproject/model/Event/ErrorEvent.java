package com.coderockets.referandumproject.model.Event;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class ErrorEvent {
    private String errorCode = null;
    private String errorTitle = null;
    private String errorContent = null;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }
}
