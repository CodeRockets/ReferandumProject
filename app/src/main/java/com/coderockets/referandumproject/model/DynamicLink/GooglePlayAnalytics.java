
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GooglePlayAnalytics {

    @SerializedName("utmSource")
    @Expose
    private String utmSource;
    @SerializedName("utmMedium")
    @Expose
    private String utmMedium;
    @SerializedName("utmCampaign")
    @Expose
    private String utmCampaign;
    @SerializedName("utmTerm")
    @Expose
    private String utmTerm;
    @SerializedName("utmContent")
    @Expose
    private String utmContent;
    @SerializedName("gclid")
    @Expose
    private String gclid;

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public String getUtmTerm() {
        return utmTerm;
    }

    public void setUtmTerm(String utmTerm) {
        this.utmTerm = utmTerm;
    }

    public String getUtmContent() {
        return utmContent;
    }

    public void setUtmContent(String utmContent) {
        this.utmContent = utmContent;
    }

    public String getGclid() {
        return gclid;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

}
