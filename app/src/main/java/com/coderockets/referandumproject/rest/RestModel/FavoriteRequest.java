package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 6.09.2016.
 */
public class FavoriteRequest extends BaseRequest {
    /*
    {
      "question_id": "string",
      "user_id": "string",
      "client_id": 0,
      "unfavorite": true
    }
     */

    @SerializedName("question_id")
    @Expose
    private String QuestionId;

    @SerializedName("user_id")
    @Expose
    private String UserId;

    @SerializedName("client_id")
    @Expose
    private int ClientId;

    @SerializedName("unfavorite")
    @Expose
    private boolean UnFavorite;

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

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public boolean isUnFavorite() {
        return UnFavorite;
    }

    public void setUnFavorite(boolean unFavorite) {
        UnFavorite = unFavorite;
    }
}
