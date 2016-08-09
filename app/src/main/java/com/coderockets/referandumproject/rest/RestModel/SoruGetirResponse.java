package com.coderockets.referandumproject.rest.RestModel;

import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by aykutasil on 20.03.2016.
 */
public class SoruGetirResponse extends BaseResponse {

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
