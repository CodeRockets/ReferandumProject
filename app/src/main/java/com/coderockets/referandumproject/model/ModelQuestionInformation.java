package com.coderockets.referandumproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 13.03.2016.
 */
@Table(name = "Sorular", id = "_id")
public class ModelQuestionInformation extends Model implements Parcelable {

    /*
    {
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
            "no": 177,
            "abuse_count": 0,
            "favorite_count": 0,
            "asker_profile_img": "http://res.cloudinary.com/dlxdlp9jz/image/upload/v1470742541/sl42nvudxn2r6ksjrak7.jpg",
            "asker_name": "Aykut Asil",
            "friends": []
    }
    */

    @Expose
    @Column
    @SerializedName("id")
    private String SoruId;

    @Expose
    @Column
    @SerializedName("question_text")
    private String QuestionText;

    @Expose
    @Column
    @SerializedName("question_image")
    private String QuestionImage;

    @Expose
    @Column
    @SerializedName("user_id")
    private String UserId;

    @Expose
    @Column
    @SerializedName("app")
    private int App;

    @Expose
    @Column
    @SerializedName("option_a")
    private String Option_A;

    @Expose
    @Column
    @SerializedName("option_b")
    private String Option_B;

    @Expose
    @Column
    @SerializedName("option_a_count")
    private int Option_A_Count;

    @Expose
    @Column
    @SerializedName("option_b_count")
    private int Option_B_Count;

    @Expose
    @Column
    @SerializedName("skip_count")
    private int Skip_Count;

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
    @SerializedName("no")
    private int No;

    @Expose
    @Column
    @SerializedName("abuse_count")
    private int AbuseCount;

    @Expose
    @Column
    @SerializedName("favorite_count")
    private int FavoriteCount;

    @Expose
    @Column
    @SerializedName("asker_profile_img")
    private String AskerProfileImg;

    @Expose
    @Column
    @SerializedName("asker_name")
    private String AskerName;

    @Expose
    @Column
    @SerializedName("friends")
    private String[] Friens;


    public ModelQuestionInformation() {
        super();
    }

    protected ModelQuestionInformation(Parcel in) {
        SoruId = in.readString();
        QuestionText = in.readString();
        QuestionImage = in.readString();
        UserId = in.readString();
        App = in.readInt();
        Option_A = in.readString();
        Option_B = in.readString();
        Option_A_Count = in.readInt();
        Option_B_Count = in.readInt();
        Skip_Count = in.readInt();
        CreatedAt = in.readString();
        UpdatedAt = in.readString();
        IsDeleted = in.readByte() != 0;
        No = in.readInt();
        AbuseCount = in.readInt();
        FavoriteCount = in.readInt();
        AskerProfileImg = in.readString();
        AskerName = in.readString();
        Friens = in.createStringArray();
    }

    public static final Creator<ModelQuestionInformation> CREATOR = new Creator<ModelQuestionInformation>() {
        @Override
        public ModelQuestionInformation createFromParcel(Parcel in) {
            return new ModelQuestionInformation(in);
        }

        @Override
        public ModelQuestionInformation[] newArray(int size) {
            return new ModelQuestionInformation[size];
        }
    };

    public int getSkip_Count() {
        return Skip_Count;
    }

    public void setSkip_Count(int skip_Count) {
        Skip_Count = skip_Count;
    }

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

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
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

    public int getAbuseCount() {
        return AbuseCount;
    }

    public void setAbuseCount(int abuseCount) {
        AbuseCount = abuseCount;
    }

    public int getFavoriteCount() {
        return FavoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        FavoriteCount = favoriteCount;
    }

    public String getAskerProfileImg() {
        return AskerProfileImg;
    }

    public void setAskerProfileImg(String askerProfileImg) {
        AskerProfileImg = askerProfileImg;
    }

    public String getAskerName() {
        return AskerName;
    }

    public void setAskerName(String askerName) {
        AskerName = askerName;
    }

    public String[] getFriens() {
        return Friens;
    }

    public void setFriens(String[] friens) {
        Friens = friens;
    }

    public String getSoruId() {
        return SoruId;
    }

    public void setSoruId(String soruId) {
        SoruId = soruId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(SoruId);
        parcel.writeString(QuestionText);
        parcel.writeString(QuestionImage);
        parcel.writeString(UserId);
        parcel.writeInt(App);
        parcel.writeString(Option_A);
        parcel.writeString(Option_B);
        parcel.writeInt(Option_A_Count);
        parcel.writeInt(Option_B_Count);
        parcel.writeInt(Skip_Count);
        parcel.writeString(CreatedAt);
        parcel.writeString(UpdatedAt);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeInt(No);
        parcel.writeInt(AbuseCount);
        parcel.writeInt(FavoriteCount);
        parcel.writeString(AskerProfileImg);
        parcel.writeString(AskerName);
        parcel.writeStringArray(Friens);
    }

    /*
    @Override
    public int compareTo(@NonNull ModelQuestionInformation modelQuestionInformation) {

        Date date = new Date(getCreatedAt());
        Date date2 = new Date(modelQuestionInformation.getCreatedAt());

        if (date.getTime() > date2.getTime()) {
            return 0;
        } else {
            return 1;
        }
    }
    */
}
