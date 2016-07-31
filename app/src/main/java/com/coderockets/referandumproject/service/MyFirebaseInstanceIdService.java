package com.coderockets.referandumproject.service;

import com.coderockets.referandumproject.model.Event.FcmRegistraionIDEvent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

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

        FcmRegistraionIDEvent fcmRegistraionIDEvent = new FcmRegistraionIDEvent();
        fcmRegistraionIDEvent.setRegID(refreshedToken);

        EventBus.getDefault().post(fcmRegistraionIDEvent);

        /*
        ModelSweetLocPreference modelSweetLocPreference = DbManager.getModelSweetLocPreference();
        if (modelSweetLocPreference == null) {
            modelSweetLocPreference = new ModelSweetLocPreference();
        }
        modelSweetLocPreference.setRegId(refreshedToken);
        modelSweetLocPreference.save();
        */
        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }
}
