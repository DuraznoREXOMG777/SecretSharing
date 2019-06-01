package com.github.durazno.secretsharing.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(Constants.NOTIFICATION, 0);

        SessionManagement sessionManagement = new SessionManagement(context, SessionManagement.AUTHORIZATION);
        String authorization = "Bearer "+sessionManagement.getAuthorization();
        SessionManagement sessionManagement1 = new SessionManagement(context, "" + id);
        String key = sessionManagement1.getKey();
        if (!authorization.equals("") && !(key.equals(""))) {
            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sendKey(authorization, id, key);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(context, "Key sent", Toast.LENGTH_SHORT).show();

                    try {
                        ResponseBody ResponseBody = response.body();
                        if (response.code() == 200) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
