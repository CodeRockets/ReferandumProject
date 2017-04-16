
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItunesConnectAnalytics {

    @SerializedName("at")
    @Expose
    private String at;
    @SerializedName("ct")
    @Expose
    private String ct;
    @SerializedName("mt")
    @Expose
    private String mt;
    @SerializedName("pt")
    @Expose
    private String pt;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

}
