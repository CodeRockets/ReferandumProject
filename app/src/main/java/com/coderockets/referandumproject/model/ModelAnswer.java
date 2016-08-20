package com.coderockets.referandumproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 2.05.2016.
 */
public class ModelAnswer extends BaseModel {
    /*
    data": {
            "id": "f0c236d0-0765-11e6-826e-b336e65223ae",
            "created_at": "2016-04-21T02:08:29.000Z",
            "updated_at": "2016-04-21T02:08:29.000Z",
            "is_deleted": false,
            "installation_id": "asdasdasdu88asd",
            "option": "a",
            "question_id": "89160c90-0676-11e6-9165-7f13b5d750e4",
            "user_id": "2d0387a0-0682-11e6-a473-e30ed6cf5986",
            "text": "http://res.cloudinary.com/dlxdlp9jz/image/upload/v1461101685/udr8d8sxx0h9myuzzp94.jpg",
            "client_id": "1"
    }
    */

    @Expose
    @SerializedName("option")
    private String Option;

    @Expose
    @SerializedName("text")
    private String Text;

    public ModelAnswer() {
        super();
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
