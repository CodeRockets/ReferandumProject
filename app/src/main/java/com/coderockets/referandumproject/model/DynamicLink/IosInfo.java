
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IosInfo {

    @SerializedName("iosBundleId")
    @Expose
    private String iosBundleId;
    @SerializedName("iosFallbackLink")
    @Expose
    private String iosFallbackLink;
    @SerializedName("iosCustomScheme")
    @Expose
    private String iosCustomScheme;
    @SerializedName("iosIpadFallbackLink")
    @Expose
    private String iosIpadFallbackLink;
    @SerializedName("iosIpadBundleId")
    @Expose
    private String iosIpadBundleId;
    @SerializedName("iosAppStoreId")
    @Expose
    private String iosAppStoreId;

    public String getIosBundleId() {
        return iosBundleId;
    }

    public void setIosBundleId(String iosBundleId) {
        this.iosBundleId = iosBundleId;
    }

    public String getIosFallbackLink() {
        return iosFallbackLink;
    }

    public void setIosFallbackLink(String iosFallbackLink) {
        this.iosFallbackLink = iosFallbackLink;
    }

    public String getIosCustomScheme() {
        return iosCustomScheme;
    }

    public void setIosCustomScheme(String iosCustomScheme) {
        this.iosCustomScheme = iosCustomScheme;
    }

    public String getIosIpadFallbackLink() {
        return iosIpadFallbackLink;
    }

    public void setIosIpadFallbackLink(String iosIpadFallbackLink) {
        this.iosIpadFallbackLink = iosIpadFallbackLink;
    }

    public String getIosIpadBundleId() {
        return iosIpadBundleId;
    }

    public void setIosIpadBundleId(String iosIpadBundleId) {
        this.iosIpadBundleId = iosIpadBundleId;
    }

    public String getIosAppStoreId() {
        return iosAppStoreId;
    }

    public void setIosAppStoreId(String iosAppStoreId) {
        this.iosAppStoreId = iosAppStoreId;
    }

}
