package com.coderockets.referandumproject.rest;

import android.content.Context;

import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.RestModel.AnswerRequest;
import com.coderockets.referandumproject.rest.RestModel.AnswerResponse;
import com.coderockets.referandumproject.rest.RestModel.ImageUploadResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruGetirBaseResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.rest.RestModel.SoruSorResponse;
import com.coderockets.referandumproject.rest.RestModel.UserQuestionsResponse;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.rest.RestModel.UserResponse;

import java.util.Map;

import hugo.weaving.DebugLog;
import okhttp3.RequestBody;
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

    @DebugLog
    public Observable<SoruSorResponse> SoruSor(SoruSorRequest soruSorRequest) {

        RestClient restClient = RestClient.getInstance();

        return restClient.getApiService()
                .SoruSor(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        SuperHelper.getDeviceId(mContext),
                        soruSorRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AnswerResponse> Answer(AnswerRequest answerRequest) {

        return RestClient.getInstance().getApiService()
                .Answer(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        com.coderockets.referandumproject.helper.SuperHelper.getDeviceId(mContext),
                        answerRequest
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SoruGetirBaseResponse> SoruGetir(int count) {
        return RestClient.getInstance().getApiService()
                .SoruGetir(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        SuperHelper.getDeviceId(mContext),
                        String.valueOf(count),
                        SuperHelper.checkUser() ? DbManager.getModelUser().getUserId() : ""
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UserResponse> SaveUser(UserRequest userRequest) {
        return RestClient.getInstance().getApiService()
                .User(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        SuperHelper.getDeviceId(mContext),
                        userRequest
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ImageUploadResponse> ImageUpload(Map<String, RequestBody> map) {
        return RestClient.getInstance().getApiService()
                .ImageUpload(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        SuperHelper.getDeviceId(mContext),
                        map
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<UserQuestionsResponse> UserQuestions(int limit) {

        ModelUser modelUser = DbManager.getModelUser();

        return RestClient.getInstance().getApiService()
                .UserQuestions(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        SuperHelper.getDeviceId(mContext),
                        0,
                        limit,
                        modelUser.getUserId()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        //
        // dfsdf
    }
    /*
    public static Observable<String> uploadImage() {



        return Observable.create((Observable.OnSubscribe<String>)
                subscriber -> {

                });
    }
    */
}
