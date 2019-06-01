package com.github.durazno.secretsharing.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.github.durazno.secretsharing.utils.SessionManagement;

public class OnClearFragmentService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext(), SessionManagement.AUTHORIZATION);
        sessionManagement.setLogged(0);
        super.onTaskRemoved(rootIntent);
    }
}
