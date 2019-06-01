package com.github.durazno.secretsharing.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://2c03905a.ngrok.io/api/auth/";
    private static RetrofitClient mIntance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mIntance == null) {
            mIntance = new RetrofitClient();
        }
        return mIntance;
    }

    public IApi getApi(){
        return retrofit.create(IApi.class);
    }
}
