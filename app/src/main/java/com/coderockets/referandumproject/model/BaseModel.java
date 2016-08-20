package com.coderockets.referandumproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 2.05.2016.
 */
public class BaseModel {

    @Expose
    @SerializedName("created_at")
    private String CreatedAt;

    @Expose
    @SerializedName("updated_at")
    private String UpdatedAt;

    @Expose
    @SerializedName("is_deleted")
    private boolean IsDeleted;

    @Expose
    @SerializedName("question_id")
    private String QuestionId;

    @Expose
    @SerializedName("user_id")
    private String UserId;

    @Expose
    @SerializedName("installation_id")
    private String InstallationId;

    @Expose
    @SerializedName("client_id")
    private String ClientId;


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

    public void setIsDeleted(boolean isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getInstallationId() {
        return InstallationId;
    }

    public void setInstallationId(String installationId) {
        InstallationId = installationId;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }
}
