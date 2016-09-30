package com.coderockets.referandumproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 30.09.2016.
 */

public class ModelFriend {
        /*
        {
            "name": "Eyüp Ferhat Güdücü",
            "option": "a",
            "profile_img": "http://res.cloudinary.com/dlxdlp9jz/image/upload/v1474197552/bxf6p5qpr8vgar0fuayq.jpg",
            "facebook_id": "10154914805130139"
          }
         */

    public ModelFriend() {
    }

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("option")
    @Expose
    private String Option;

    @SerializedName("profile_img")
    @Expose
    private String ProfileImage;

    @SerializedName("facebook_id")
    @Expose
    private String FacebookId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getFacebookId() {
        return FacebookId;
    }

    public void setFacebookId(String facebookId) {
        FacebookId = facebookId;
    }
}
