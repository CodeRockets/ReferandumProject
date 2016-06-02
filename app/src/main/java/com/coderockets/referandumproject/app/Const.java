package com.coderockets.referandumproject.app;


import com.coderockets.referandumproject.BuildConfig;

/**
 * Created by aykutasil on 12.03.2016.
 */
public class Const {

    public static final String CLOSE = "Close";
    public static final String SORU_SOR = "SoruSor";
    public static final String BOOK = "Book";
    public static final String SORULAR = "Sorular";
    public static final String CASE = "Case";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";
    public static final String SETTINGS = "Settings";


    public static final String CLIENT_ID = "0";
    public static final String REFERANDUM_VERSION = "1";
    //public static final String INSTALLATION = "246";


    ////
    public static final String REAKTIF_APP_VERSION = String.valueOf(BuildConfig.VERSION_CODE);
    public static final String REAKTIF_API_VERSION = "1";

    public static final String REAKTIF_PROCESS_NAME_ZIYARET = "Ziyaret";
    public static final String REAKTIF_PROCESS_NAME_IADE = "Iade";
    public static final String REAKTIF_PROCESS_NAME_TESLIM = "Teslim";
    public static final String REAKTIF_PROCESS_NAME_RANDEVU = "Randevu";

    public static final int REQUEST_PROGRESS = 1;
    public static final int REQUEST_LIST_SIMPLE = 9;
    public static final int REQUEST_LIST_MULTIPLE = 10;
    public static final int REQUEST_LIST_SINGLE = 11;
    public static final int REQUEST_DATE_PICKER = 12;
    public static final int REQUEST_TIME_PICKER = 13;
    public static final int REQUEST_SIMPLE_DIALOG = 42;
    public static final int ACTIVITY_REQUEST_CODE_LOGIN = 905;
    public static final int ACTIVITY_RESULT_CODE_SIGNUP = 646;


    public static final int ADMIN_ACTIVATED_REQUEST = 657;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SAMPLE_PERIODIC_TAG = "sample_tag";

    public static final String IZTOP_DENEME_PERIODIC_TASK = "denemeperiyodiktask";

    public static final String REAKTIF_TASK_TYPE = "TaskType";
    public static final String REAKTIF_TASK_UUID = "TaskUUID";
    public static final String REAKTIF_TASK_ZIYARET = "TaskZiyaret";
    public static final String REAKTIF_TASK_IADE = "TaskIade";
    public static final String REAKTIF_TASK_RANDEVU = "TaskRandevu";
    public static final String REAKTIF_TASK_TESLIM = "TaskTeslim";
    public static final String REAKTIF_TASK_TOPLUTESLIM = "TaskTopluTeslim";
    public static final String REAKTIF_TASK_BELGE = "TaskBelge";
    public static final String REAKTIF_TASK_UPDATE_PERSONEL = "TaskUpdatePersonel";
    public static final String REAKTIF_TASK_PHONE_CALL = "TaskPhoneCall";
    public static final String REAKTIF_TASK_PHONE_CALL_STATE = "TaskPhoneCallState";

    public static final String REAKTIF_TASK_BUNDLE = "IztopBundle";
    public static final String REAKTIF_TASK_TAG = "ReaktifTaskTag";
    public static final int REAKTIF_TASK_TRY_COUNT = 5;
    public static final String REAKTIF_TASK_STATUS_BEKLEMEDE = "TaskBeklemede";
    public static final String REAKTIF_TASK_STATUS_AKTARILDI = "TaskAktarildi";
    public static final String REAKTIF_TASK_STATUS_IPTAL = "TaskIptal";

    public static final String GCM_DOWNLOAD_EXTERNAL = "external";
    public static final String GCM_DOWNLOAD_MARKET = "market";
    public static final String GCM_PUSH_ACTION = "PushAction";
    public static final String GCM_PUSH_NOTIF_TITLE = "NotifTitle";
    public static final String GCM_PUSH_NOTIF_CONTENT = "NotifContent";
    public static final String GCM_PUSH_NOTIF_SUBCONTENT = "NotifSubContent";
    public static final String GCM_PUSH_NOTIF_NO_CLEAR = "NotifNoClear";
    public static final String GCM_PUSH_DOWNLOAD_APP_TYPE = "DownloadAppType";
    public static final String GCM_PUSH_DOWNLOAD_FILE_NAME = "DownloadFileName";
    public static final String GCM_PUSH_DOWNLOAD_PATH = "DownloadPath";
    public static final String GCM_PUSH_SHOW_NOTIF = "ShowNotif";
    public static final String GCM_PUSH_SCREEN_PASSWORD = "ScreenPass";
    public static final String GCM_PUSH_DOWNLOAD_REQUIRE_WIRELESS = "RequireWireless";

    public static final String GCM_PUSH_ALIKO_ADSOYAD = "AlikoAdSoyad";
    public static final String GCM_PUSH_ALIKO_ACIKLAMA = "AlikoAciklama";
    public static final String GCM_PUSH_ALIKO_SAAT = "AlikoSaat";
    public static final String GCM_PUSH_ALIKO_ALICI_CEPTEL = "AlikoAliciCepTel";
    public static final String GCM_PUSH_ALIKO_GONDERI_NO = "AlikoGonderiNo";
    public static final String GCM_PUSH_ALIKO_MUS_AD_CINS = "AlikoMusAdCins";


    public static final String GCM_ACTION_UPDATE = "ActionUpdate";
    public static final String GCM_ACTION_REAKTIF_RESET = "ActionReaktifReset";
    public static final String GCM_ACTION_DOWNLOAD_INSTALL_APP = "ActionDownloadAndInstallApp";
    public static final String GCM_ACTION_ZIMMETI_KALDIR = "ActionZimmetiKaldir";
    public static final String GCM_ACTION_ALIKO = "ActionAlikoNotif";
    public static final String GCM_ACTION_CHANGE_SCREEN_PASSWORD = "ActionChangeScreenPassword";
    public static final String GCM_ACTION_CLEAR_ALL_GCMTASK = "ActionClearAllGcmTask";
    public static final String GCM_ACTION_STOP_FOREGROUND_SERVICE = "ActionStopForegroundService";


    public static final int REAKTIF_ERROR_SIGNIN_GONDERILIST_SIZE = 1005;
    public static final int REAKTIF_ERROR_SIGNIN_DATA_NULL = 1006;
    public static final int REAKTIF_ERROR_SIGNIN_STATUS = 1007;

    public static final int ACTIVITY_RESULT_DIGI_SCAN_BELGE = 676;

    public static class Prefs {
        public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
        public static final String SAVE_GCM_TOKEN = "save_gcn_token";
        public static final String HIGH_ACCURACY = "is_high_accuracy";
        public static final String PERIODIC_TIME = "periodşc_time";
    }


}
