package com.coderockets.referandumproject.rest.RestModel;

import android.content.Context;

import com.aykuttasil.androidbasichelperlib.SuperHelper;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class SoruSorRequest extends BaseRequest {

    /*
    {
            "user_id": "string",
            "app": 0,
            "question_text": "string",
            "question_image": "string",
            "option_a": "string",
            "option_b": "string"
    }
    */

    @SerializedName("user_id")
    private String UserId;

    @SerializedName("question_text")
    private String QuestionText;

    @SerializedName("question_image")
    private String QuestionImage;

    @SerializedName("option_a")
    private String Option_A;

    @SerializedName("option_b")
    private String Option_B;

    private static SoruSorRequest GetRequest(Context context) {

        SoruSorRequest request = new SoruSorRequest();
        request.setUserId(SuperHelper.getDeviceId(context));
        return request;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        this.QuestionText = questionText;
    }

    public String getQuestionImage() {
        return QuestionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.QuestionImage = questionImage;
    }
}
