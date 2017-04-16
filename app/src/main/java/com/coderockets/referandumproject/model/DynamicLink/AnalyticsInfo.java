
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnalyticsInfo {

    @SerializedName("googlePlayAnalytics")
    @Expose
    private GooglePlayAnalytics googlePlayAnalytics;
    @SerializedName("itunesConnectAnalytics")
    @Expose
    private ItunesConnectAnalytics itunesConnectAnalytics;

    public GooglePlayAnalytics getGooglePlayAnalytics() {
        return googlePlayAnalytics;
    }

    public void setGooglePlayAnalytics(GooglePlayAnalytics googlePlayAnalytics) {
        this.googlePlayAnalytics = googlePlayAnalytics;
    }

    public ItunesConnectAnalytics getItunesConnectAnalytics() {
        return itunesConnectAnalytics;
    }

    public void setItunesConnectAnalytics(ItunesConnectAnalytics itunesConnectAnalytics) {
        this.itunesConnectAnalytics = itunesConnectAnalytics;
    }

}
