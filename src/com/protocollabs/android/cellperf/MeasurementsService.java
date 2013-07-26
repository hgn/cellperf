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


    Thread mThread = new Thread() {

        volatile boolean mShouldStop = false;

        @Override
        public void run() {
            try {

                while (!mShouldStop) {
                    sleep(60 * 1000);
                    
                    String data = Pinger.ping("heise.de");
                    measurementsReady(data);
                }
            } catch (InterruptedException e) {
                Log.v(TAG ,"Thread interrupted..." );
            }
        }

    };


    @Override
    public void onCreate() {
        Toast.makeText(this, "Measurements activated", Toast.LENGTH_SHORT).show();
        mThread.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
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


    private void measurementsReady(String data) {
        Log.i(TAG, "measurementsReady");
        Intent intent = new Intent("measurement-data-ready");
        intent.putExtra("ping-measurement", data);
        sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Measurements deactivated", Toast.LENGTH_SHORT).show();
        mThread.interrupt();
    }
}
