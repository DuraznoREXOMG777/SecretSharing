package com.github.durazno.secretsharing.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private Timer timer = new Timer();
    private Handler handler;

    private static final long INTERVALO_ACTUALIZACION = 100;

    String token;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        token = intent.getStringExtra("token");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);;
            }
        };
        iniciarCronometro();
    }

    private void iniciarCronometro() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                handler.sendEmptyMessage(0);
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }
}
