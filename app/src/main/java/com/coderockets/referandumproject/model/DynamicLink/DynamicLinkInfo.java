
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamicLinkInfo {

    @SerializedName("dynamicLinkDomain")
    @Expose
    private String dynamicLinkDomain;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("androidInfo")
    @Expose
    private AndroidInfo androidInfo;
    @SerializedName("iosInfo")
    @Expose
    private IosInfo iosInfo;
    @SerializedName("analyticsInfo")
    @Expose
    private AnalyticsInfo analyticsInfo;
    @SerializedName("socialMetaTagInfo")
    @Expose
    private SocialMetaTagInfo socialMetaTagInfo;

    public String getDynamicLinkDomain() {
        return dynamicLinkDomain;
    }

    public void setDynamicLinkDomain(String dynamicLinkDomain) {
        this.dynamicLinkDomain = dynamicLinkDomain;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AndroidInfo getAndroidInfo() {
        return androidInfo;
    }

    public void setAndroidInfo(AndroidInfo androidInfo) {
        this.androidInfo = androidInfo;
    }

    public IosInfo getIosInfo() {
        return iosInfo;
    }

    public void setIosInfo(IosInfo iosInfo) {
        this.iosInfo = iosInfo;
    }

    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo;
    }

    public void setAnalyticsInfo(AnalyticsInfo analyticsInfo) {
        this.analyticsInfo = analyticsInfo;
    }

    public SocialMetaTagInfo getSocialMetaTagInfo() {
        return socialMetaTagInfo;
    }

    public void setSocialMetaTagInfo(SocialMetaTagInfo socialMetaTagInfo) {
        this.socialMetaTagInfo = socialMetaTagInfo;
    }

}
