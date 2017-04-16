package com.coderockets.referandumproject.service;

import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.model.ModelUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 20.11.2015.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @DebugLog
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Refreshed token: " + refreshedToken);

        ModelUser modelUser = DbManager.getModelUser();

        if (modelUser != null) {
            modelUser.setRegId(refreshedToken);
            modelUser.save();
        }
    }
}
