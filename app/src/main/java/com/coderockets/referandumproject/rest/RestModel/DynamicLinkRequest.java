
package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.BuildConfig;
import com.coderockets.referandumproject.model.DynamicLink.AndroidInfo;
import com.coderockets.referandumproject.model.DynamicLink.DynamicLinkInfo;
import com.coderockets.referandumproject.model.DynamicLink.SocialMetaTagInfo;
import com.coderockets.referandumproject.model.DynamicLink.Suffix;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import hugo.weaving.DebugLog;

public class DynamicLinkRequest {

    @SerializedName("dynamicLinkInfo")
    @Expose
    private DynamicLinkInfo dynamicLinkInfo;

    @SerializedName("suffix")
    @Expose
    private Suffix suffix;

    public DynamicLinkInfo getDynamicLinkInfo() {
        return dynamicLinkInfo;
    }

    public void setDynamicLinkInfo(DynamicLinkInfo dynamicLinkInfo) {
        this.dynamicLinkInfo = dynamicLinkInfo;
    }

    public Suffix getSuffix() {
        return suffix;
    }

    public void setSuffix(Suffix suffix) {
        this.suffix = suffix;
    }


    @DebugLog
    public static DynamicLinkRequest getDynamicLink(String link, String socialTitle, String socialDesc, String socialImgUrl) {
        DynamicLinkRequest dynamicLinkRequest = new DynamicLinkRequest();

        Suffix suffix = new Suffix();
        suffix.setOption("SHORT");
        dynamicLinkRequest.setSuffix(suffix);

        DynamicLinkInfo dynamicLinkInfo = new DynamicLinkInfo();
        dynamicLinkInfo.setLink(link);

        AndroidInfo androidInfo = new AndroidInfo();
        androidInfo.setAndroidPackageName(BuildConfig.APPLICATION_ID);
        androidInfo.setAndroidMinPackageVersionCode("21");
        //androidInfo.setAndroidLink("");
        dynamicLinkInfo.setAndroidInfo(androidInfo);


        SocialMetaTagInfo socialMetaTagInfo = new SocialMetaTagInfo();
        socialMetaTagInfo.setSocialTitle(socialTitle);
        socialMetaTagInfo.setSocialDescription(socialDesc);
        socialMetaTagInfo.setSocialImageLink(socialImgUrl);
        dynamicLinkInfo.setSocialMetaTagInfo(socialMetaTagInfo);

        dynamicLinkRequest.setDynamicLinkInfo(dynamicLinkInfo);
        return dynamicLinkRequest;
    }
}
