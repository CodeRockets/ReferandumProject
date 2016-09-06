package com.coderockets.referandumproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aykutasil on 6.09.2016.
 */
public class ModelUserQuestions {


    @SerializedName("questions")
    @Expose
    private Questions Questions;


    @SerializedName("favorites")
    @Expose
    private Favorites Favorites;

    public ModelUserQuestions.Questions getQuestions() {
        return Questions;
    }

    public void setQuestions(ModelUserQuestions.Questions questions) {
        Questions = questions;
    }

    public ModelUserQuestions.Favorites getFavorites() {
        return Favorites;
    }

    public void setFavorites(ModelUserQuestions.Favorites favorites) {
        Favorites = favorites;
    }

    public class Questions {

        @Expose
        @SerializedName("count")
        private int Count;

        @Expose
        @SerializedName("rows")
        private List<ModelQuestionInformation> Rows;

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }

        public List<ModelQuestionInformation> getRows() {
            return Rows;
        }

        public void setRows(List<ModelQuestionInformation> rows) {
            Rows = rows;
        }
    }

    public class Favorites {

        @Expose
        @SerializedName("count")
        private int Count;

        @Expose
        @SerializedName("rows")
        private List<ModelQuestionInformation> Rows;

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }

        public List<ModelQuestionInformation> getRows() {
            return Rows;
        }

        public void setRows(List<ModelQuestionInformation> rows) {
            Rows = rows;
        }
    }
}
