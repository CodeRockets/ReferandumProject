package com.coderockets.referandumproject.rest;

import android.content.Context;

import com.aykuttasil.androidbasichelperlib.SuperHelper;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.rest.RestModel.SoruSorResponse;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 2.06.2016.
 */
public class ApiManager {

    private static ApiManager mInstance;
    private Context mContext;
    private String abc;


    private ApiManager(Context context) {
        mContext = context;
    }

    @DebugLog
    public static ApiManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ApiManager(context);
        }
        return mInstance;
    }

    //region SoruSor
    @DebugLog
    public void SoruSor(SoruSorRequest soruSorRequest) {

        Logger.i("UserId: " + soruSorRequest.getUserId());

        try {
            RestClient restClient = RestClient.getInstance();

            Observable<SoruSorResponse> api = restClient.getApiService().SoruSor(
                    Const.CLIENT_ID,
                    Const.REFERANDUM_VERSION,
                    SuperHelper.getDeviceId(mContext),
                    soruSorRequest);

            api.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(soruSorResponse -> {
                        //UiHelper.UiSnackBar.showSimpleSnackBar();
                        UiHelper.UiDialog.newInstance(mContext).getOKDialog("Uyyy", soruSorResponse.getData().getQuestionText(), null).show();
                    }, error -> {
                        UiHelper.UiDialog.newInstance(mContext).getOKDialog("Uyyy", error.getMessage(), null).show();

                    });

        } catch (Exception e) {
            e.printStackTrace();
            UiHelper.UiDialog.newInstance(mContext).getOKDialog("Uyyy", e.getMessage(), null).show();

            /*
            ErrorEvent errorEvent = new ErrorEvent();
            errorEvent.setErrorTitle("Hata");
            errorEvent.setErrorContent(e.getMessage());
            errorEvent.setErrorCode("99");
            EventBus.getDefault().post(errorEvent);
            */
        }
    }
    //endregion
}
