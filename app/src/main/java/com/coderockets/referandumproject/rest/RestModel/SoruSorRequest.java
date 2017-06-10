package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.model.ModelUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
public class SoruSorRequest extends BaseRequest {

    @Expose
    @SerializedName("user_id")
    private String UserId;

    @Expose
    @SerializedName("app")
    private int App;

    @Expose
    @SerializedName("question_text")
    private String QuestionText;

    @Expose
    @SerializedName("question_image")
    private String QuestionImage;

    @Expose
    @SerializedName("option_a")
    private String Option_A;

    @Expose
    @SerializedName("option_b")
    private String Option_B;

    @Expose
    @SerializedName("is_private")
    private boolean IsPrivate;

    @Expose
    @SerializedName("private_url")
    private String PrivateUrl;

    @Expose
    @SerializedName("notify_friend")
    private boolean NotifyFriend;

    private SoruSorRequest() {
    }

    public static SoruSorRequest SoruSorRequestInstance() {
        ModelUser modelUser = DbManager.getModelUser();
        SoruSorRequest request = new SoruSorRequest();
        request.setUserId(modelUser.getUserId());
        request.setApp(Const.REFERANDUM_APP);
        request.setPrivate(false);
        request.setNotifyFriend(false);
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

    public String getOption_A() {
        return Option_A;
    }

    public void setOption_A(String option_A) {
        Option_A = option_A;
    }

    public String getOption_B() {
        return Option_B;
    }

    public void setOption_B(String option_B) {
        Option_B = option_B;
    }

    public int getApp() {
        return App;
    }

    public void setApp(int app) {
        App = app;
    }

    public boolean isPrivate() {
        return IsPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        IsPrivate = aPrivate;
    }

    public String getPrivateUrl() {
        return PrivateUrl;
    }

    public void setPrivateUrl(String privateUrl) {
        PrivateUrl = privateUrl;
    }

    public boolean isNotifyFriend() {
        return NotifyFriend;
    }

    public void setNotifyFriend(boolean notifyFriend) {
        NotifyFriend = notifyFriend;
    }
}
