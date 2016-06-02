package com.coderockets.referandumproject.model;

import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
@Table(name = "User", id = "_id")
public class ModelUser extends BaseModel {

    /*{
            "id": "c94642f0-fd0f-11e5-9a28-a3a2789bd42e",
            "created_at": "2016-04-07T22:26:34.000Z",
            "updated_at": "2016-04-07T22:26:34.000Z",
            "is_deleted": false,
            "facebook_id": "10153036713185139",
            "name": "Eyüp Ferhat Güdücü",
            "profile_img": "http://res.cloudinary.com/dlxdlp9jz/image/upload/v1460068234/w9uanjdmtg86xebceoqv.jpg"
    }*/

//    @SerializedName("id")
//    private String Id;
//
//    @SerializedName("created_at")
//    private String CreatedAt;
//
//    @SerializedName("updated_at")
//    private String UpdatedAt;
//
//    @SerializedName("is_deleted")
//    private boolean IsDeleted;

    @SerializedName("facebook_id")
    private String FacebookId;

    @SerializedName("name")
    private String Name;

    @SerializedName("profile_img")
    private String ProfileImageUrl;


    public ModelUser() {
        super();
    }


//    public String getUserId() {
//        return Id;
//    }
//
//    public void setUserId(String id) {
//        Id = id;
//    }
//
//    public String getCreatedAt() {
//        return CreatedAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        CreatedAt = createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return UpdatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        UpdatedAt = updatedAt;
//    }
//
//    public boolean isDeleted() {
//        return IsDeleted;
//    }
//
//    public void setIsDeleted(boolean isDeleted) {
//        IsDeleted = isDeleted;
//    }

    public String getFacebookId() {
        return FacebookId;
    }

    public void setFacebookId(String facebookId) {
        FacebookId = facebookId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
    }
}
