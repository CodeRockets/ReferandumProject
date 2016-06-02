package com.coderockets.referandumproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.Event.ErrorEvent;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by aykutasil on 20.11.2015.
 */
public class RegistrationIntentService extends IntentService {


    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);
            //subscribeTopics(token);

            //PrefsHelper.writePrefBool(this, Const.Prefs.SAVE_GCM_TOKEN, true);

            /*
            GcmRegistraionIDEvent gcmRegistraionIDEvent = new GcmRegistraionIDEvent();
            gcmRegistraionIDEvent.setRegID(token);
            gcmRegistraionIDEvent.setStatus(200);
            EventBus.getDefault().postSticky(gcmRegistraionIDEvent);
            */

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            //PrefsHelper.writePrefBool(this, Const.Prefs.SENT_TOKEN_TO_SERVER, false);

            ErrorEvent errorEvent = new ErrorEvent();
            errorEvent.setErrorCode("300");
            errorEvent.setErrorContent(e.getMessage());
            errorEvent.setErrorTitle("RegistrationIntentService");
            EventBus.getDefault().post(errorEvent);


            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //sharedPreferences.edit().putBoolean(Const.Prefs.SENT_TOKEN_TO_SERVER, false).apply();

        }
    }

//    /**
//     * Persist registration to third-party servers.
//     * <p/>
//     * Modify this method to associate the user's GCM registration token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private GcmRegistraionIDEvent sendRegistrationToServer(String token, Bundle bundle) {
//        String pKod = bundle.getString("PKod");
//        String pPass = bundle.getString("Pass");
//        String pTelNo = bundle.getString("TelNo");
//
//        //return ReaktifApiManager.SaveGcmToServer(token, pKod, pPass);
//        return null;
//    }

    //    /**
//     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
//     *
//     * @param token GCM token
//     * @throws IOException if unable to reach the GCM PubSub service
//     */
//    // [START subscribe_topics]
   /* private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);

        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
            Log.i(TAG, "GcmPubSub: " + "/topics/" + topic);
        }
    }*/
//    // [END subscribe_topics]
}
