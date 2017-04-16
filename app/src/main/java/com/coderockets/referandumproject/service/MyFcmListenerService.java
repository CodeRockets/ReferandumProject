package com.coderockets.referandumproject.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 4.07.2016.
 */
public class MyFcmListenerService extends FirebaseMessagingService {


    private static final String PUSH_ACTION = "PushAction";
    private static final String PUSH_ACTION_FRIEND_ASK_QUESTION = "ActionFriendAskQuestion";

    @DebugLog
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Logger.i("data: " + remoteMessage.getData());

        if (remoteMessage.getData().size() > 0) {
            switch (remoteMessage.getData().get(PUSH_ACTION)) {
                case PUSH_ACTION_FRIEND_ASK_QUESTION: {
                    EventBus.getDefault().post(remoteMessage);
                    break;
                }
            }
        }


        /*
        RemoteMessage.Notification notification =  message.getNotification();

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(notification.getTitle())
                .setSmallIcon(R.drawable.ic_account_circle_pink_900_48dp)
                .setContentText(notification.getBody());
                */
    }

    @DebugLog
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
