package com.coderockets.referandumproject.rest;

import android.content.Context;

import com.aykuttasil.androidbasichelperlib.PrefsHelper;
import com.coderockets.referandumproject.BuildConfig;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.RestModel.AnswerRequest;
import com.coderockets.referandumproject.rest.RestModel.AnswerResponse;
import com.coderockets.referandumproject.rest.RestModel.DynamicLinkRequest;
import com.coderockets.referandumproject.rest.RestModel.DynamicLinkResponse;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.rest.RestModel.FavoriteResponse;
import com.coderockets.referandumproject.rest.RestModel.ImageUploadResponse;
import com.coderockets.referandumproject.rest.RestModel.ImgurImageResponse;
import com.coderockets.referandumproject.rest.RestModel.QuestionDeleteResponse;
import com.coderockets.referandumproject.rest.RestModel.ReportAbuseRequest;
import com.coderockets.referandumproject.rest.RestModel.ReportAbuseResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruGetirBaseResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.rest.RestModel.SoruSorResponse;
import com.coderockets.referandumproject.rest.RestModel.UserQuestionsResponse;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.rest.RestModel.UserResponse;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

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
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        soruSorRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<AnswerResponse> Answer(AnswerRequest answerRequest) {

        return RestClient.getInstance().getApiService()
                .Answer(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        answerRequest
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<SoruGetirBaseResponse> SoruGetir(int count) {

        return RestClient.getInstance().getApiService()
                .SoruGetir(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        String.valueOf(count),
                        SuperHelper.checkUser() ? DbManager.getModelUser().getUserId() : "",
                        BuildConfig.DEBUG ? "1" : "0"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<UserResponse> SaveUser(UserRequest userRequest) {

        Logger.i("UserRequest: " + new Gson().toJson(userRequest));

        return RestClient.getInstance().getApiService()
                .User(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        userRequest
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @DebugLog
    public Observable<ImageUploadResponse> ImageUpload(Map<String, RequestBody> map) {

        return RestClient.getInstance().getApiService()
                .ImageUpload(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        map
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @DebugLog
    public Observable<UserQuestionsResponse> UserQuestions(int limit) {

        ModelUser modelUser = DbManager.getModelUser();

        return RestClient.getInstance().getApiService()
                .UserQuestions(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        0,
                        limit,
                        modelUser.getUserId()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<FavoriteResponse> Favorite(FavoriteRequest request) {

        return RestClient.getInstance().getApiService()
                .Favorite(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        request
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<ReportAbuseResponse> ReportAbuse(ReportAbuseRequest request) {

        return RestClient.getInstance().getApiService()
                .ReportAbuse(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        request
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<QuestionDeleteResponse> QuestionDelete(String questionId) {

        return RestClient.getInstance().getApiService()
                .QuestionDelete(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        questionId
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<ImgurImageResponse> ImgurImageUpload(String title, String desc, String albumId, String username, RequestBody image) {

        return RestClient.getInstance().getApiService()
                .ImgurPostImage(
                        "Client-ID " + Const.IMGUR_CLIENT_ID,
                        title,
                        desc,
                        null,
                        username,
                        image
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @DebugLog
    public Observable<SoruGetirBaseResponse> TekSoruGetir(String questionId) {

        return RestClient.getInstance().getApiService()
                .TekSoruGetir(
                        Const.CLIENT_ID,
                        Const.REFERANDUM_VERSION,
                        PrefsHelper.readPrefString(mContext, Const.PREFS_KEY_REFERANDUM_UUID),
                        questionId,
                        String.valueOf(Const.REFERANDUM_APP),
                        SuperHelper.checkUser() ? DbManager.getModelUser().getUserId() : ""
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @DebugLog
    public Observable<DynamicLinkResponse> DynamicLink(DynamicLinkRequest request) {
        return RestClient.getInstance().getApiService()
                .DynamicLink(request);
    }
}
