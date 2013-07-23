package com.protocollabs.android.cellperf;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.HandlerThread;
import android.os.SystemClock;

import android.util.Log;

import android.os.Process;



// http://developer.android.com/guide/components/services.html
// http://www.vogella.com/articles/AndroidServices/article.html
// http://dharmendra4android.blogspot.de/2012/05/bind-service-using-ibinder-class.html


public class MeasurementsService extends Service {

    private final IBinder mBinder = new MeasurementsBinder();

    private final String TAG = getClass().getSimpleName();


    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while(true) {
                    sleep(10000);
                    measurementsReady();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };





    @Override
    public void onCreate() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();


        thread.start();


        // For each start request, send a message to start a job and deliver the

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }


    public class MeasurementsBinder extends Binder {
        MeasurementsService getService() {
            return MeasurementsService.this;
        }
    }


    public String getMeasurementsResults() {
        return "results";
    }

    private void measurementsReady() {
        Log.i(TAG, "measurementsReady");
        Intent intent = new Intent("measurement-data-ready");
        intent.putExtra("ping-measurement", "ready");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        // to shut up the thread call
        // mHandlerThread.quit();
    }

    // TO BE IMPLEMENTED
    //
    // onCreate()
    // implementieren und den measurement thread spawnen
    // onDestroy()
    // implementieren um darin gestartete threads zu beednden
}
