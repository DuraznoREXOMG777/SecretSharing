package com.github.durazno.secretsharing.services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.durazno.secretsharing.utils.NotificationHelper;
import com.github.durazno.secretsharing.utils.SessionManagement;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            SessionManagement sessionManagement = new SessionManagement(getApplicationContext(), (String) values.get(0));
            sessionManagement.setID((String) values.get(0));
            sessionManagement.setKey((String) values.get(1));

            NotificationHelper.displayNotification(getApplicationContext(), title, body);
        }
    }
}
