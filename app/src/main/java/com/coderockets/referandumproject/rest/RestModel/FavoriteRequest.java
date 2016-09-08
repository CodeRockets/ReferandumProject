package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.model.ModelUser;
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


    private FavoriteRequest() {
    }

    public static FavoriteRequest FavoriteRequestInit() {

        ModelUser modelUser = DbManager.getModelUser();

        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUserId(modelUser.getUserId());
        favoriteRequest.setClientId(Integer.parseInt(Const.CLIENT_ID));

        return favoriteRequest;
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
