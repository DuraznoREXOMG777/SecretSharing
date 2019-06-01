package com.github.durazno.secretsharing.services;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.durazno.secretsharing.SessionActivity;
import com.github.durazno.secretsharing.models.AuthResponse;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.utils.NotificationHelper;
import com.github.durazno.secretsharing.utils.RetrofitClient;
import com.github.durazno.secretsharing.utils.SessionManagement;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseNotifications extends FirebaseMessagingService {

    private static final String TAG = "FirebaseNotifications";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            List values = new ArrayList<>(remoteMessage.getData().values());


            String idProj = (String) values.get(0);
            String projectKey = (String) values.get(1);
            if (projectKey.equals("1")) {
                NotificationHelper.displayNotificationButton(getApplicationContext(), title, body, Integer.parseInt(idProj));
            } else {
                SessionManagement sessionManagement = new SessionManagement(getApplicationContext(), (String) values.get(0));
                sessionManagement.setID(idProj);
                sessionManagement.setKey(projectKey);
                NotificationHelper.displayNotification(getApplicationContext(), title, body);
            }


        }
    }
}
