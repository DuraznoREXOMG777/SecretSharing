package com.github.durazno.secretsharing.utils;

import com.github.durazno.secretsharing.models.AuthResponse;
import com.github.durazno.secretsharing.models.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IApi {

    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> login(@Field("email") String email, @Field("password") String password, @Field("device_token") String token);

    @Headers("Content-Type: application/json")
    @GET("user")
    Call<UserResponse> user(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("projects/second-leader")
    Call<ResponseBody> projectsSecond(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("projects/researcher")
    Call<ResponseBody> projectsResearcher(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("projects/leader")
    Call<ResponseBody> projectsLeader(@Header("Authorization") String token);


}
