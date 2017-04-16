
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AndroidInfo {

    @SerializedName("androidPackageName")
    @Expose
    private String androidPackageName;
    @SerializedName("androidFallbackLink")
    @Expose
    private String androidFallbackLink;
    @SerializedName("androidMinPackageVersionCode")
    @Expose
    private String androidMinPackageVersionCode;
    @SerializedName("androidLink")
    @Expose
    private String androidLink;

    public String getAndroidPackageName() {
        return androidPackageName;
    }

    public void setAndroidPackageName(String androidPackageName) {
        this.androidPackageName = androidPackageName;
    }

    public String getAndroidFallbackLink() {
        return androidFallbackLink;
    }

    public void setAndroidFallbackLink(String androidFallbackLink) {
        this.androidFallbackLink = androidFallbackLink;
    }

    public String getAndroidMinPackageVersionCode() {
        return androidMinPackageVersionCode;
    }

    public void setAndroidMinPackageVersionCode(String androidMinPackageVersionCode) {
        this.androidMinPackageVersionCode = androidMinPackageVersionCode;
    }

    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }

}
