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


            switch (bundle.getString(Const.REAKTIF_TASK_TYPE)) {
                /*
                case Const.REAKTIF_TASK_ZIYARET: {
                    return SchedulerZiyaretTask(bundle);
                }
                case Const.REAKTIF_TASK_IADE: {
                    return SchedulerIadeTask(bundle);
                }
                case Const.REAKTIF_TASK_RANDEVU: {
                    return SchedulerRandevuTask(bundle);
                }
                case Const.REAKTIF_TASK_TESLIM: {
                    return SchedulerTeslimTask(bundle);
                }
                case Const.REAKTIF_TASK_TOPLUTESLIM: {
                    return SchedulerTopluTeslimTask(bundle);
                }
                case Const.REAKTIF_TASK_UPDATE_PERSONEL: {
                    return SchedulerUpdatePersonelTask(bundle);
                }
                case Const.REAKTIF_TASK_BELGE: {
                    return SchedulerSendBelge(bundle);
                }
                case Const.REAKTIF_TASK_PHONE_CALL: {
                    return SchedulerPhoneCall(bundle);
                }
                case Const.REAKTIF_TASK_PHONE_CALL_STATE: {
                    return SchedulerPhoneCallState(bundle);
                }
                */


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

    /*
    @DebugLog
    private int SchedulerZiyaretTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            ZiyaretRequest request = new Gson().fromJson(jsonModel, ZiyaretRequest.class);
            request.setProcessDate(SuperHelper.getFormatTime());
            ZiyaretResponse ziyaretResponse = sendZiyaretProcess(request);
            switch (ziyaretResponse.getCode()) {
                case 0: {
                    EventBus.getDefault().post(ziyaretResponse);
                    NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                            .setNotifContent(request.getGonderiNo() + " numaralı gönderinin Ziyaret işlemi tamamlanmıştır.")
                            .setNotifTitle("Ziyaret")
                            .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                            .setNotifId((new Random().nextInt() + new Random().nextInt()))
                            .buildNotif();
                    mNotificationHelper.showNotif();

                    ReaktifApiManager.getInstance(getApplicationContext()).SignIn();
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }

        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;
    }

    @DebugLog
    private int SchedulerIadeTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            IadeRequest request = new Gson().fromJson(jsonModel, IadeRequest.class);
            request.setProcessDate(SuperHelper.getFormatTime());
            IadeResponse response = sendIadeProcess(request);
            switch (response.getCode()) {
                case 0: {
                    EventBus.getDefault().post(response);
                    NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                            .setNotifContent(request.getGonderiNo() + " numaralı gönderinin İade işlemi tamamlanmıştır.")
                            .setNotifTitle("İade")
                            .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                            .setNotifId((new Random().nextInt() + new Random().nextInt()))
                            .buildNotif();
                    mNotificationHelper.showNotif();

                    ReaktifApiManager.getInstance(getApplicationContext()).SignIn();
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }


        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;

    }

    @DebugLog
    private int SchedulerRandevuTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            RandevuRequest request = new Gson().fromJson(jsonModel, RandevuRequest.class);
            request.setProcessDate(SuperHelper.getFormatTime());
            RandevuResponse randevuResponse = sendRandevuProcess(request);
            switch (randevuResponse.getCode()) {
                case 0: {
                    EventBus.getDefault().post(randevuResponse);
                    //UiHelper.UiToast.showSimpleToast(getApplicationContext(), String.valueOf(modelGonderi.getGonderiNo()), Toast.LENGTH_LONG);
                    //Toast.makeText(getApplicationContext(), modelGonderi.getAliciAd(), Toast.LENGTH_LONG).show();
                    NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                            .setNotifContent(request.getGonderiNo() + " numaralı gönderinin Randevu işlemi tamamlanmıştır.")
                            .setNotifTitle("Randevu")
                            .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                            .setNotifId((new Random().nextInt() + new Random().nextInt()))
                            .buildNotif();
                    mNotificationHelper.showNotif();

                    ReaktifApiManager.getInstance(getApplicationContext()).SignIn();
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }

        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;

    }

    @DebugLog
    private int SchedulerTeslimTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            TeslimRequest request = new Gson().fromJson(jsonModel, TeslimRequest.class);
            request.setProcessDate(SuperHelper.getFormatTime());
            TeslimResponse teslimResponse = sendTeslimProcess(request);
            switch (teslimResponse.getCode()) {
                case 0: {
                    EventBus.getDefault().post(teslimResponse);
                    //UiHelper.UiToast.showSimpleToast(getApplicationContext(), String.valueOf(modelGonderi.getGonderiNo()), Toast.LENGTH_LONG);
                    //Toast.makeText(getApplicationContext(), modelGonderi.getAliciAd(), Toast.LENGTH_LONG).show();

                    NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                            .setNotifContent(request.getTeslimAlanAd() + " " + request.getTeslimAlanSoyad())
                            .setNotifTitle("Teslim")
                            .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                            .setNotifId((new Random().nextInt() + new Random().nextInt()))
                            .buildNotif();
                    mNotificationHelper.showNotif();

                    ReaktifApiManager.getInstance(getApplicationContext()).SignIn();
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }


        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;

    }

    @DebugLog
    private int SchedulerTopluTeslimTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            TopluTeslimRequest request = new Gson().fromJson(jsonModel, TopluTeslimRequest.class);
            request.setProcessDate(SuperHelper.getFormatTime());
            TopluTeslimResponse topluTeslimResponse = sendTopluTeslimProcess(request);
            switch (topluTeslimResponse.getCode()) {
                case 0: {
                    EventBus.getDefault().post(topluTeslimResponse);
                    //UiHelper.UiToast.showSimpleToast(getApplicationContext(), String.valueOf(modelGonderi.getGonderiNo()), Toast.LENGTH_LONG);
                    //Toast.makeText(getApplicationContext(), modelGonderi.getAliciAd(), Toast.LENGTH_LONG).show();

                    NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                            .setNotifContent(request.getTeslimAlanAd() + " " + request.getTeslimAlanSoyad())
                            .setNotifTitle("Toplu Teslim")
                            .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                            .setNotifId((new Random().nextInt() + new Random().nextInt()))
                            .buildNotif();
                    mNotificationHelper.showNotif();

                    ReaktifApiManager.getInstance(getApplicationContext()).SignIn();
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }


        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;

    }

    @DebugLog
    private int SchedulerUpdatePersonelTask(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            UpdatePersonelRequest request = new Gson().fromJson(jsonModel, UpdatePersonelRequest.class);
            UpdatePersonelResponse updatePersonelResponse = sendUpdatePersonelProcess(request);

            switch (updatePersonelResponse.getCode()) {
                case 0: {
                    modelGcmTask.delete();
                    EventBus.getDefault().post(updatePersonelResponse);
                    EventBus.getDefault().post(modelGcmTask);
                    //UiHelper.UiToast.showSimpleToast(getApplicationContext(), String.valueOf(modelGonderi.getGonderiNo()), Toast.LENGTH_LONG);
                    //Toast.makeText(getApplicationContext(), modelGonderi.getAliciAd(), Toast.LENGTH_LONG).show();
                    //NotificationHelper.showNotificationMessage(getApplicationContext(), "Ziyaret", request.getNot(), new Random().nextInt(), null);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }

        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }


        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;
    }

    @DebugLog
    private int SchedulerSendBelge(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            BelgeRequest request = new Gson().fromJson(jsonModel, BelgeRequest.class);
            BelgeResponse response = sendBelgeProcess(request);
            switch (response.getCode()) {
                case 0: {
                    try {
                        EventBus.getDefault().post(response);

                        InformationEvent informationEvent = new InformationEvent();
                        informationEvent.setTitle("Belge Aktarım");
                        informationEvent.setMessage(request.getGonderiNo() + " numaralı gönderinin Belge aktarımı tamamlanmıştır.");
                        EventBus.getDefault().post(informationEvent);

                        NotificationHelper mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                                .setNotifContent(request.getGonderiNo() + " numaralı gönderinin Belge aktarımı tamamlanmıştır.")
                                .setNotifTitle("Belge Aktarım")
                                .setNotifIcon(R.drawable.ic_stat_editor_attach_file)
                                .setNotifId((new Random().nextInt() + new Random().nextInt()))
                                .buildNotif();
                        mNotificationHelper.showNotif();

                        String belgeDirPath = SuperHelper.getInternalReaktifBelgeDir(getApplicationContext(), request.getGonderiNo());
                        File belgeDir = new File(belgeDirPath);
                        FileUtils.deleteDirectory(belgeDir);

                        modelGcmTask.delete();
                        EventBus.getDefault().post(modelGcmTask);
                        return GcmNetworkManager.RESULT_SUCCESS;
                    } catch (Exception e) {
                        SuperHelper.CrashlyticsLog(e);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }

        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() > Const.REAKTIF_TASK_TRY_COUNT) {
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;
    }

    @DebugLog
    private int SchedulerPhoneCall(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            PhoneCallRequest request = new Gson().fromJson(jsonModel, PhoneCallRequest.class);
            PhoneCallResponse randevuResponse = sendPhoneCallProcess(request);
            switch (randevuResponse.getCode()) {
                case 0: {
                    //EventBus.getDefault().post(randevuResponse);
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }


        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;
    }

    @DebugLog
    private int SchedulerPhoneCallState(Bundle bundle) {
        String jsonModel = null;
        String uuid = null;
        ModelGcmTask modelGcmTask = null;
        try {
            jsonModel = bundle.getString(Const.REAKTIF_TASK_BUNDLE);
            uuid = bundle.getString(Const.REAKTIF_TASK_UUID);
            modelGcmTask = DbManager.getGcmTaskFromId(uuid);

            PhoneCallStateRequest request = new Gson().fromJson(jsonModel, PhoneCallStateRequest.class);
            PhoneCallStateResponse randevuResponse = sendPhoneCallStateProcess(request);

            switch (randevuResponse.getCode()) {
                case 0: {
                    modelGcmTask.delete();
                    EventBus.getDefault().post(modelGcmTask);
                    return GcmNetworkManager.RESULT_SUCCESS;
                }
            }
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }

        modelGcmTask.setTaskTryCount(modelGcmTask.getTaskTryCount() + 1);
        modelGcmTask.setTaskDate(SuperHelper.getFormatTime());
        modelGcmTask.save();

        if (modelGcmTask.getTaskTryCount() == Const.REAKTIF_TASK_TRY_COUNT) {
            //DbManager.dbDeleteGcmTask(jsonModel);
            modelGcmTask.setTaskStatus(Const.REAKTIF_TASK_STATUS_IPTAL);
            modelGcmTask.save();
            EventBus.getDefault().post(modelGcmTask);
            return GcmNetworkManager.RESULT_FAILURE;
        }
        EventBus.getDefault().post(modelGcmTask);
        return GcmNetworkManager.RESULT_RESCHEDULE;
    }


    //////////////


    @DebugLog
    private ZiyaretResponse sendZiyaretProcess(ZiyaretRequest ziyaretRequest) {
        return ReaktifApiManager.getInstance(getApplicationContext()).ZiyaretSync(ziyaretRequest);
    }

    @DebugLog
    private IadeResponse sendIadeProcess(IadeRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).IadeSync(request);
    }

    @DebugLog
    private RandevuResponse sendRandevuProcess(RandevuRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).RandevuSync(request);
    }

    @DebugLog
    private TeslimResponse sendTeslimProcess(TeslimRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).TeslimSync(request);
    }

    @DebugLog
    private TopluTeslimResponse sendTopluTeslimProcess(TopluTeslimRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).TopluTeslimSync(request);
    }

    @DebugLog
    private UpdatePersonelResponse sendUpdatePersonelProcess(UpdatePersonelRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).UpdatePersonelSync(request);
    }

    @DebugLog
    private BelgeResponse sendBelgeProcess(BelgeRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).BelgeSync(request);
    }

    @DebugLog
    private PhoneCallResponse sendPhoneCallProcess(PhoneCallRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).PhoneCallSync(request);
    }

    @DebugLog
    private PhoneCallStateResponse sendPhoneCallStateProcess(PhoneCallStateRequest request) {
        return ReaktifApiManager.getInstance(getApplicationContext()).PhoneCallStateSync(request);
    }

    */
}
