package com.coderockets.referandumproject.rest;

import com.coderockets.referandumproject.rest.RestModel.ImageUploadResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruGetirBaseResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.rest.RestModel.SoruSorResponse;
import com.coderockets.referandumproject.rest.RestModel.SorularResponse;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.rest.RestModel.UserResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by aykutasil on 2.06.2016.
 */
public interface ApiService {

    @POST("v1/user")
    Observable<UserResponse> User(@Header("x-voter-client-id") String clientId,
                                  @Header("x-voter-version") String version,
                                  @Header("x-voter-installation") String installation,
                                  @Body UserRequest userRequest);

    @POST("v1/question")
    Observable<SoruSorResponse> SoruSor(@Header("x-voter-client-id") String clientId,
                                        @Header("x-voter-version") String version,
                                        @Header("x-voter-installation") String installation,
                                        @Body SoruSorRequest soruSorRequest);


    @GET("v1/question/all")
    Call<SorularResponse> Sorular();


    @GET("v1/question/fetch/0")
    Call<SoruGetirBaseResponse> SoruGetir(@Header("x-voter-client-id") String clientId,
                                          @Header("x-voter-version") String version,
                                          @Header("x-voter-installation") String installation,
                                          @Query(value = "limit") String limit,
                                          @Query(value = "user_id") String userId);

    @Multipart
    @POST("v1/question/image")
    Observable<ImageUploadResponse> ImageUpload(@Header("x-voter-client-id") String clientId,
                                                @Header("x-voter-version") String version,
                                                @Header("x-voter-installation") String installation,
                                                @PartMap Map<String, RequestBody> Files);


    //@Multipart
    //@POST("api/belge")
    //Call<BelgeResponse> Belge(@Header("X-Api-Version") String apiVersion, @Header("X-App-Version") String appVersion, @Part(value = "file\"; filename=\"GrandCanyon.jpg") RequestBody Files);

    //@Multipart
    //@POST("api/belge")
    //Call<BelgeResponse> Belge(@Header("X-Api-Version") String apiVersion, @Header("X-App-Version") String appVersion, @Part("gonderiNo") RequestBody gonderiNo, @Part("musNo") RequestBody musId, @PartMap Map<String, RequestBody> Files);


    /*@Multipart
    @POST("api/belge")
    Call<BelgeResponse> Belge(@Header("X-Api-Version") String apiVersion,
                              @Header("X-App-Version") String appVersion,
                              @Header("X-Personel-Kod") String personelKod,
                              @Header("X-Pass") String personelPass,
                              @Header("X-Tel-No") String telNo,
                              @Header("X-Sim-Id") String simId,
                              @Query("gonderiNo") String gonderiNo,
                              @Query("musId") String musId,
                              @Query("mok") String mok,
                              @PartMap Map<String, RequestBody> Files);
*/
}
