package com.coderockets.referandumproject.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 20.11.2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    //private NotificationHelper mNotificationHelper;

    @DebugLog
    @Override
    public void onMessageReceived(String from, Bundle data) {

    /*    try {
            String action = data.getString(GCM_PUSH_ACTION);
            String notifTitle = data.getString(GCM_PUSH_NOTIF_TITLE);
            String notifContent = data.getString(GCM_PUSH_NOTIF_CONTENT);
            String notifSubContent = data.getString(Const.GCM_PUSH_NOTIF_SUBCONTENT);
            boolean notifNoClear = Boolean.parseBoolean(data.getString(GCM_PUSH_NOTIF_NO_CLEAR));
            boolean isShowNotif = Boolean.parseBoolean(data.getString(GCM_PUSH_SHOW_NOTIF));

            SuperHelper.CrashlyticsLogInfo("GcmPushAction", action);
            Log.d(TAG, "PushAction: " + action);

            Intent intent_ = new Intent(getApplicationContext(), MainActivity_.class);
            intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent mainIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent_, PendingIntent.FLAG_ONE_SHOT);


            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setAutoCancel(true)
                    .setContentText(notifContent)
                    .setContentTitle(notifTitle)
                    .setSubText(notifSubContent)
                    .setOngoing(notifNoClear)
                    .setContentIntent(mainIntent)
                    .setSmallIcon(R.drawable.ic_suggestion);


            *//*mNotificationHelper = NotificationHelper.newInstance(getApplicationContext(), 1)
                    .setNotifTitle(notifTitle)
                    .setNotifContent(notifContent)
                    .setNotifSubContent(notifSubContent)
                            //.setNotifContentIntent(mainIntent)
                    .buildNotif()
                    .setNotifNoClear(notifNoClear);*//*


            switch (action) {
                case GCM_ACTION_UPDATE: {
                    if (isShowNotif) {
                        showNotif();
                    }

                    break;
                }
                case Const.GCM_ACTION_REAKTIF_RESET: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    SuperHelper.ReaktifReset(getApplicationContext());
                    break;
                }
                case Const.GCM_ACTION_CHANGE_SCREEN_PASSWORD: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    String screenPass = data.getString(Const.GCM_PUSH_SCREEN_PASSWORD);
                    //String newPass = SuperHelper.generateRandom(4);

                    String newPass = screenPass;
                    ModelPersonel modelPersonel = DbManager.getPersonel();
                    modelPersonel.setScreenPassword(newPass);
                    modelPersonel.save();

                    ReaktifDevicePolicyManager.getInstance(getApplicationContext()).ReaktifSetPassword(newPass);
                    SuperHelper.UpdatePersonelAsync(getApplicationContext(), ReaktifRequest.EUpdatePart.ScreenPasswordUpdate);
                    ReaktifDevicePolicyManager.getInstance(getApplicationContext()).screenLock();

                    break;
                }
                case GCM_ACTION_DOWNLOAD_INSTALL_APP: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    try {
                        String downloadAppType = data.getString(GCM_PUSH_DOWNLOAD_APP_TYPE);
                        String downloadFileName = data.getString(GCM_PUSH_DOWNLOAD_FILE_NAME);
                        String downloadPath = data.getString(GCM_PUSH_DOWNLOAD_PATH);


                        boolean requireWireless = Boolean.parseBoolean(data.getString(Const.GCM_PUSH_DOWNLOAD_REQUIRE_WIRELESS));
                        if (requireWireless) {
                            if (!InternetConnectionHelper.getWifiIsConnected(getApplicationContext())) {
                                Handler hnd = new Handler(Looper.getMainLooper());
                                hnd.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        UiHelper.UiToast.showSimpleToast(getApplicationContext(), "Wifi Bağlantısını Açınız !", Toast.LENGTH_LONG);
                                    }
                                });

                                break;
                            }
                        }

                        switch (downloadAppType) {
                            case GCM_DOWNLOAD_EXTERNAL: {
                                SuperHelper.DownloadAndInstallApp(getApplicationContext(), GCM_DOWNLOAD_EXTERNAL, downloadPath, downloadFileName);
                                break;
                            }
                            case GCM_DOWNLOAD_MARKET: {
                                SuperHelper.DownloadAndInstallApp(getApplicationContext(), GCM_DOWNLOAD_MARKET, downloadPath, downloadFileName);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        SuperHelper.CrashlyticsLog(e);
                    }
                    break;
                }
                case GCM_ACTION_ZIMMETI_KALDIR: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    break;
                }
                case GCM_ACTION_ALIKO: {
                    //if (isShowNotif) {
                    showNotif();
                    //}

                    SuperHelper.CrashlyticsLogInfo("GcmPushActionAliko", data.getString(Const.GCM_PUSH_ALIKO_ACIKLAMA));
                    Intent intent = getIntentKuryeIletisim(data);
                    intent.setClass(getApplicationContext(), KuryeIletisimActivity_.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                }
                case Const.GCM_ACTION_CLEAR_ALL_GCMTASK: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    GcmNetworkManager.getInstance(getApplicationContext()).cancelAllTasks(MyGcmTaskService.class);
                    break;
                }
                case Const.GCM_ACTION_STOP_FOREGROUND_SERVICE: {
                    if (isShowNotif) {
                        showNotif();
                    }
                    ForegroundService.StopReaktifService();
                    break;
                }

            }


            //String message = data.getString("message");
            if (from.startsWith("/topics/")) {
                //
            } else if (from.startsWith("/topics/denemetopic")) {
                //
            }

            //NotificationHelper.showNotificationMessage(getApplicationContext(), "Gcm Message", message, 123, null);

            // GcmEvent gcmEvent = new GcmEvent();
            // gcmEvent.setMessage(message);
            // EventBus.getDefault().post(gcmEvent);

            //sendNotification(message);
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }*/
    }


    private void showNotif() {
        notificationManager.notify(new Random().nextInt(), mBuilder.build());
        //mNotificationHelper.showNotif();
    }

    private Intent getIntentKuryeIletisim(Bundle data) {
        Intent kurye_intent = null;
        /*try {
            // Alici Not
            String alert = data.getString(Const.GCM_PUSH_ALIKO_ACIKLAMA);

            //Alici Ad Soyad - Secenek(AdresteYok,Maruzat)
            String title = data.getString(Const.GCM_PUSH_ALIKO_ADSOYAD);

            //Notification gönderilme tarihi
            String saat = data.getString(Const.GCM_PUSH_ALIKO_SAAT);

            //Alici cep tel
            String alici_ceptel = data.getString(Const.GCM_PUSH_ALIKO_ALICI_CEPTEL);

            //Gonderi No
            String gonderi_no = data.getString(Const.GCM_PUSH_ALIKO_GONDERI_NO);

            //Musteri Adi - Gonderi Cinsi (Akbank - Kredi Kartı)
            String musteri_adi_cinsi = data.getString(Const.GCM_PUSH_ALIKO_MUS_AD_CINS);


            kurye_intent = new Intent();

            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_ADSOYAD, title);
            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_ACIKLAMA, alert);
            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_SAAT, saat);
            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_ALICI_CEPTEL, alici_ceptel);
            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_GONDERI_NO, gonderi_no);
            kurye_intent.putExtra(Const.GCM_PUSH_ALIKO_MUS_AD_CINS, musteri_adi_cinsi);


        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }*/
        return kurye_intent;

    }

}
