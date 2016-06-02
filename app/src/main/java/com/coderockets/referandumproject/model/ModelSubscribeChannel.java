package com.coderockets.referandumproject.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 28.01.2016.
 */
@Table(name = "SubscribeChannel")
public class ModelSubscribeChannel extends Model {

    @SerializedName("ChannelName")
    @Column
    String ChannelName;

    public ModelSubscribeChannel() {
        super();
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }
}
