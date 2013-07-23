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

import android.support.v4.content.LocalBroadcastManager;


// http://developer.android.com/guide/components/services.html
// http://www.vogella.com/articles/AndroidServices/article.html
// http://dharmendra4android.blogspot.de/2012/05/bind-service-using-ibinder-class.html


public class MeasurementsService extends Service {

    private final IBinder mBinder = new MeasurementsBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
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
        Intent intent = new Intent("measurement-data-ready");
        intent.putExtra("ping-measurement", "ready");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // TO BE IMPLEMENTED
    //
    // onCreate()
    // implementieren und den measurement thread spawnen
    // onDestroy()
    // implementieren um darin gestartete threads zu beednden
}

