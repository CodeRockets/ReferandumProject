package com.coderockets.referandumproject.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
@Table(name = "User", id = "_id")
public class ModelUser extends Model {

    /*{
            "id": "c94642f0-fd0f-11e5-9a28-a3a2789bd42e",
            "created_at": "2016-04-07T22:26:34.000Z",
            "updated_at": "2016-04-07T22:26:34.000Z",
            "is_deleted": false,
            "facebook_id": "10153036713185139",
            "name": "Eyüp Ferhat Güdücü",
            "profile_img": "http://res.cloudinary.com/dlxdlp9jz/image/upload/v1460068234/w9uanjdmtg86xebceoqv.jpg"
    }*/

    @Expose
    @Column
    @SerializedName("id")
    private String UserId;

    @Expose
    @Column
    @SerializedName("created_at")
    private String CreatedAt;

    @Expose
    @Column
    @SerializedName("updated_at")
    private String UpdatedAt;

    @Expose
    @Column
    @SerializedName("is_deleted")
    private boolean IsDeleted;

    @Expose
    @Column
    @SerializedName("facebook_id")
    private String FacebookId;

    @Expose
    @Column
    @SerializedName("name")
    private String Name;

    @Expose
    @Column
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }
}
