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


public class CellInformation implements Parcelable {

    public String measurementDate;
    public String coordinateLangitude;
    public String coordinateLongitude;
    public int dbm;
    private int currentSignalStrength = NeighboringCellInfo.UNKNOWN_RSSI;
    private TelephonyManager telephonyManager = null;

    private Context context;
    private static Context globalContext = null;


    public CellInformation(Context context) {
        this.context = context;
    }

    public static synchronized void setGlobalContext(Context newGlobalContext) {
        globalContext = newGlobalContext;
    }

    public synchronized void initNetwork() {
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new SignalStrengthChangeListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
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

    private static final String[] NETWORK_TYPES = {
        "UNKNOWN",  // 0  - NETWORK_TYPE_UNKNOWN
        "GPRS",     // 1  - NETWORK_TYPE_GPRS
        "EDGE",     // 2  - NETWORK_TYPE_EDGE
        "UMTS",     // 3  - NETWORK_TYPE_UMTS
        "CDMA",     // 4  - NETWORK_TYPE_CDMA
        "EVDO_0",   // 5  - NETWORK_TYPE_EVDO_0
        "EVDO_A",   // 6  - NETWORK_TYPE_EVDO_A
        "1xRTT",    // 7  - NETWORK_TYPE_1xRTT
        "HSDPA",    // 8  - NETWORK_TYPE_HSDPA
        "HSUPA",    // 9  - NETWORK_TYPE_HSUPA
        "HSPA",     // 10 - NETWORK_TYPE_HSPA
        "IDEN",     // 11 - NETWORK_TYPE_IDEN
        "EVDO_B",   // 12 - NETWORK_TYPE_EVDO_B
        "LTE",      // 13 - NETWORK_TYPE_LTE
        "EHRPD",    // 14 - NETWORK_TYPE_EHRPD
    };

    public synchronized void setCurrentRssi(int rssi) {
        currentSignalStrength = rssi;
    }

    public synchronized int getCurrentRssi() {
        return currentSignalStrength;
    }


    public String getNetwork() {
        initNetwork();
        return getTelephonyNetworkType();
    }

    private String getTelephonyNetworkType() {
        assert NETWORK_TYPES[14].compareTo("EHRPD") == 0;

        int networkType = telephonyManager.getNetworkType();
        if (networkType < NETWORK_TYPES.length) {
            return NETWORK_TYPES[telephonyManager.getNetworkType()];
        } else {
            return "Unrecognized: " + networkType;
        }
    }

    private String getTelephonyPhoneType() {
        switch (telephonyManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "None";
        }
        return "Unknown";
    }


    private class SignalStrengthChangeListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_CDMA]) == 0) {
                setCurrentRssi(signalStrength.getCdmaDbm());
            } else if (getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_0]) == 0 ||
                    getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_A]) == 0 ||
                    getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_B]) == 0) {
                setCurrentRssi(signalStrength.getEvdoDbm());
            } else if (signalStrength.isGsm()) {
                setCurrentRssi(signalStrength.getGsmSignalStrength());
            }
        }
    }
}
