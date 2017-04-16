
package com.coderockets.referandumproject.model.DynamicLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialMetaTagInfo {

    @SerializedName("socialTitle")
    @Expose
    private String socialTitle;
    @SerializedName("socialDescription")
    @Expose
    private String socialDescription;
    @SerializedName("socialImageLink")
    @Expose
    private String socialImageLink;

    public String getSocialTitle() {
        return socialTitle;
    }

    public void setSocialTitle(String socialTitle) {
        this.socialTitle = socialTitle;
    }

    public String getSocialDescription() {
        return socialDescription;
    }

    public void setSocialDescription(String socialDescription) {
        this.socialDescription = socialDescription;
    }

    public String getSocialImageLink() {
        return socialImageLink;
    }

    public void setSocialImageLink(String socialImageLink) {
        this.socialImageLink = socialImageLink;
    }

}
