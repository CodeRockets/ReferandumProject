package com.coderockets.referandumproject.app;


import android.Manifest;

/**
 * Created by aykutasil on 12.03.2016.
 */
public class Const {

    public static final int REFERANDUM_APP = 0;
    public static final String CLIENT_ID = "0";
    public static final String REFERANDUM_VERSION = "1";

    public static final String[] PERMISSIONS_GENERAL = new String[]{Manifest.permission.READ_PHONE_STATE};
    public static final String[] PERMISSIONS_ASK_QUESTION = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };


    public static final String REFERANDUM_TASK_TYPE = "TaskType";
    public static final String PREFS_KEY_REFERANDUM_UUID = "ReferandumUUID";

    public static final String IMGUR_CLIENT_ID = "5b37683c8864f6a";
    public static final String IMGUR_CLIENT_SECRET = "6ce99211c17ccf2b58cbcd0d35e23ec8caac3a2a";


}
