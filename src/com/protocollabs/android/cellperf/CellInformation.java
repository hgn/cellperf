package  com.protocollabs.android.cellperf;


import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.os.Parcelable;
import android.os.Parcel;
import android.widget.Toast;



public class CellInformation implements Parcelable {

    private final String TAG = getClass().getSimpleName();

    public String measurementDate;
    public String coordinateLangitude;
    public String coordinateLongitude;
    public int dbm;

    private int mRssi;
    private String mNetworkType;
    private String mSimOperator;
    private String mSimOperatorName;
    private String mTelephonyNetworkType;
    private String mNetworkOperatorName;
    private String mNetworkOperator;
    private String mNetworkCountryIso;
    private String mDeviceId;
    private String mCid;
    private String mLac;

    private Context context;
    private CellInformationProvider mCellInformationProvider;
    private CellInformationFragment mCellInformationFragment;


    public CellInformation(Context context, CellInformationFragment cellInformationFragment) {
        this.context = context;
        mCellInformationFragment = cellInformationFragment;
        mCellInformationProvider = new CellInformationProvider(context, this);
    }


    public void activate() {
        Log.i(TAG, "activate");
        mCellInformationProvider.activate();
    }


    public void deactivate() {
        Log.i(TAG, "deactivate");
        mCellInformationProvider.deactivate();
    }
    

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<CellInformation> CREATOR = new Parcelable.Creator<CellInformation>() {
        public CellInformation createFromParcel(Parcel in) {
            return new CellInformation(in);
        }
        
        public CellInformation[] newArray(int size) {
            return new CellInformation[size];
        }
    };


    public void writeToParcel(Parcel out, int flags) {
        out.writeString(measurementDate);
        out.writeString(coordinateLangitude);
        out.writeString(coordinateLongitude);
        out.writeInt(dbm);
    }


    private CellInformation(Parcel in) {
        measurementDate = in.readString();
        coordinateLangitude = in.readString();
        coordinateLongitude = in.readString();
        dbm = in.readInt();
    }

    public void setSimOperator(String simOperator) {
        mSimOperator = simOperator;
    }

    public String getSimOperator() {
        return mSimOperator;
    }


    public synchronized void setCurrentRssi(int rssi) {
        mRssi = rssi;
    }

    public synchronized int getCurrentRssi() {
        return mRssi;
    }


    public synchronized void setTelephonyNetworkType(String network) {
        mTelephonyNetworkType = network;
    }


    public synchronized String getTelephonyNetworkType() {
        return mTelephonyNetworkType;
    }


    public synchronized void setSimOperatorName(String val) {
        mSimOperatorName = val;
    }

    public synchronized String getSimOperatorName() {
        return mSimOperatorName;
    }

    public synchronized void setNetworkOperatorName(String val) {
        mNetworkOperatorName = val;
    }

    public synchronized String getNetworkOperatorName() {
        return mNetworkOperatorName;
    }

    public synchronized void setNetworkOperator(String val) {
        mNetworkOperator = val;
    }

    public synchronized String getNetworkOperator() {
        return mNetworkOperator;
    }

    public synchronized void setNetworkCountryIso(String val) {
        mNetworkCountryIso = val;
    }

    public synchronized String getNetworkCountryIso() {
        return mNetworkCountryIso;
    }

    public synchronized void setDeviceId(String val) {
        mDeviceId = val;
    }

    public synchronized String getDeviceId() {
        return mDeviceId;
    }

    public synchronized void setCid(String val) {
        mCid = val;
    }

    public synchronized String getCid() {
        return mCid;
    }

    public synchronized void setLac(String val) {
        mLac = val;
    }

    public synchronized String getLac() {
        return mLac;
    }




    public void update() {
        mCellInformationFragment.update(this);
    }

}
