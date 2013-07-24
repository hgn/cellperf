package com.protocollabs.android.cellperf;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

public class CellInformationFragment extends Fragment {

    private View mView;

    private final String TAG = getClass().getSimpleName();
    private CellInformation cellInformation = null;

    private TextView rssiTextView;

    private boolean mSpeechActivated;
    private Speech mSpeech;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_cell_information, container, false);
        getActivity().setTitle("Cell Information");

        //if (savedInstanceState != null) {
        //    cellInformation = (CellInformation) savedInstanceState.getParcelable("cellInformation");
        //    if (cellInformation == null) {
        //        cellInformation = new CellInformation(this.getActivity(), this);
        //    }
        //} else {
            cellInformation = new CellInformation(this.getActivity(), this);
        //}

        int rssi = cellInformation.getCurrentRssi();

        rssiTextView = (TextView) mView.findViewById(R.id.rssi);
        rssiTextView.setText(String.valueOf(rssi));

        /*
        Button mButton = (Button) mView.findViewById(R.id.buttonrefresh);
        if (mButton != null) {
            mButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rssi = cellInformation.getCurrentRssi();
                    rssiTextView.setText(String.valueOf(rssi));
                }
            });
        }
        */

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mSpeechActivated = prefs.getBoolean("misc_cell_voice_information", false);


        if (mSpeechActivated == true) {
            mSpeech = new Speech(this.getActivity());
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.v(TAG, "onSaveInstanceState");
        state.putParcelable("cellInformation", cellInformation);
    }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "onCreate");
	}

	public void onStart() {
		super.onStart();	
		Log.i(TAG, "onStart");
        cellInformation.activate();
	}
	
	public void onresume() {
		super.onResume();

		Log.i(TAG, "onResume");
        cellInformation.activate();
        if (mSpeechActivated == true) {
            mSpeech = new Speech(this.getActivity());
        }
	}
	
	public void onPause() {
		super.onPause();
        cellInformation.deactivate();
        if (mSpeechActivated == true) {
            mSpeech.shutdown();
        }
	}
	
	public void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		Log.i(TAG, "onDestroyView");
	}
	
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
	
	public void onDetach() {
		super.onDetach();
		Log.i(TAG, "onDetach");
	}
	
	public void onActivityCreated() {
		Log.i(TAG, "onActivityCreated");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);
		Log.i(TAG, "onConfigurationChanged");
	}
	
	@Override
	public void onLowMemory() {
		Log.i(TAG, "onLowMemory");
	}


    public void update(CellInformation cellInformation) {
        int rssi;
        TextView textView;
        
        rssi = cellInformation.getCurrentRssi();
        textView = (TextView) mView.findViewById(R.id.rssi);
        textView.setText(String.valueOf(rssi));

        textView = (TextView) mView.findViewById(R.id.networktype);
        textView.setText(cellInformation.getTelephonyNetworkType());

        textView = (TextView) mView.findViewById(R.id.simoperator);
        textView.setText(cellInformation.getSimOperator());

        textView = (TextView) mView.findViewById(R.id.simoperatorname);
        textView.setText(cellInformation.getSimOperatorName());

        textView = (TextView) mView.findViewById(R.id.network_operator_name);
        textView.setText(cellInformation.getNetworkOperatorName());

        textView = (TextView) mView.findViewById(R.id.network_operator);
        textView.setText(cellInformation.getNetworkOperator());

        textView = (TextView) mView.findViewById(R.id.network_country_iso);
        textView.setText(cellInformation.getNetworkCountryIso());

        textView = (TextView) mView.findViewById(R.id.device_id);
        textView.setText(cellInformation.getDeviceId());

        textView = (TextView) mView.findViewById(R.id.cid);
        textView.setText(cellInformation.getCid());

        textView = (TextView) mView.findViewById(R.id.lac);
        textView.setText(cellInformation.getLac());

        if (mSpeech != null)
            mSpeech.speak("New signal strength " + cellInformation.getCurrentRssi() + "decibel");
    }
}
