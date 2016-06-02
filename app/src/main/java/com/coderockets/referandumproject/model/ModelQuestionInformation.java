package com.coderockets.referandumproject.model;

import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
@Table(name = "Sorular", id = "_id")
public class ModelQuestionInformation extends BaseModel {

    /*
    {
            "rn": "20",
            "id": "444c7d10-e97f-11e5-b6b4-ede54fe3ed30",
            "question_text": "Android Referandum Test Question 1",
            "question_image": "Android Referandum Test Image 1",
            "user_id": "3916ccf0-002e-11e6-893e-699e7ec1dd8e",
            "app": 0,
            "option_a": "evet",
            "option_b": "hayir",
            "option_a_count": 0,
            "option_b_count": 50,
            "skip_count": 0,
            "created_at": "2016-03-14T00:54:12.000Z",
            "updated_at": "2016-03-14T00:54:12.000Z",
            "is_deleted": false,
            "no": 177
    }
    */


    //@SerializedName("id")
    //private String Id;

    //@SerializedName("created_at")
    //private Date CreatedAt;

    //@SerializedName("updated_at")
    //private Date UpdatedAt;

    //@SerializedName("is_deleted")
    //private boolean IsDeleted;

    @SerializedName("rn")
    private String Rn;

    @SerializedName("question_text")
    private String QuestionText;

    @SerializedName("question_image")
    private String QuestionImage;

    //@SerializedName("user_id")
    //private String UserId;

    @SerializedName("app")
    private int App;

    @SerializedName("option_a")
    private String Option_A;

    @SerializedName("option_b")
    private String Option_B;

    @SerializedName("option_a_count")
    private int Option_A_Count;

    @SerializedName("option_b_count")
    private int Option_B_Count;

    @SerializedName("skip_count")
    private int Skip_Count;

    @SerializedName("no")
    private int No;


    public ModelQuestionInformation() {
        super();
    }

    /*
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }*/


    public int getSkip_Count() {
        return Skip_Count;
    }

    public void setSkip_Count(int skip_Count) {
        Skip_Count = skip_Count;
    }


//    public String getIdSoru() {
//        return Id;
//    }
//
//    public void setId(String id) {
//        Id = id;
//    }
//
//    public Date getCreatedAt() {
//        return CreatedAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        CreatedAt = createdAt;
//    }
//
//    public Date getUpdatedAt() {
//        return UpdatedAt;
//    }
//
//    public void setUpdatedAt(Date updatedAt) {
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

//    public String getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(String userId) {
//        UserId = userId;
//    }

    public int getApp() {
        return App;
    }

    public void setApp(int app) {
        App = app;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }

    public String getQuestionImage() {
        return QuestionImage;
    }

    public void setQuestionImage(String questionImage) {
        QuestionImage = questionImage;
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

    public int getOption_A_Count() {
        return Option_A_Count;
    }

    public void setOption_A_Count(int option_A_Count) {
        Option_A_Count = option_A_Count;
    }

    public int getOption_B_Count() {
        return Option_B_Count;
    }

    public void setOption_B_Count(int option_B_Count) {
        Option_B_Count = option_B_Count;
    }

    public String getRn() {
        return Rn;
    }

    public void setRn(String rn) {
        Rn = rn;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }
}
