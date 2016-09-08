package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.model.ModelUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 6.09.2016.
 */
public class ReportAbuseRequest extends BaseRequest {

    /*
    {
      "question_id": "string",
      "user_id": "string",
      "client_id": 0
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

    private ReportAbuseRequest() {
    }

    public static ReportAbuseRequest ReportAbuseRequestInit() {
        ModelUser modelUser = DbManager.getModelUser();

        ReportAbuseRequest reportAbuseRequest = new ReportAbuseRequest();
        reportAbuseRequest.setUserId(modelUser.getUserId());
        reportAbuseRequest.setClientId(Integer.parseInt(Const.CLIENT_ID));

        return reportAbuseRequest;
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
}
