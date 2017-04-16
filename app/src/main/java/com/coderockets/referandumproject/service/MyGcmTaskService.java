package com.coderockets.referandumproject.service;

import android.os.Bundle;

import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.model.Event.ErrorEvent;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;


/**
 * Created by aykutasil on 21.11.2015.
 */
public class MyGcmTaskService extends GcmTaskService {

    private static String TAG = MyGcmTaskService.class.getSimpleName();

    @DebugLog
    @Override
    public void onInitializeTasks() {
    }

    @DebugLog
    @Override
    public int onRunTask(TaskParams taskParams) {
        try {
            Bundle bundle = taskParams.getExtras();

            switch (bundle.getString(Const.REFERANDUM_TASK_TYPE)) {
                default: {
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            ErrorEvent errorEvent = new ErrorEvent();
            errorEvent.setErrorContent(e.getMessage());
            EventBus.getDefault().post(errorEvent);
            return GcmNetworkManager.RESULT_RESCHEDULE;
        }
    }
}
