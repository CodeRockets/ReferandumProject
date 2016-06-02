package com.coderockets.referandumproject.rest.RestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 1.05.2016.
 */
public class AnswerRequest extends BaseRequest {

    /*
    {
            "option": "string",
            "question_id": "string",
            "user_id": "string",
            "text": "string",
            "client_id": 0
    }
    */

    @SerializedName("option")
    private String Option;

    @SerializedName("question_id")
    private String QuestionId;

    @SerializedName("user_id")
    private String UserId;

    @SerializedName("text")
    private String Text;

    @SerializedName("client_id")
    private int ClientId;

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
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

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }
}
