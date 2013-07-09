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
import android.util.Log;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class CellInformationFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_cell_information, container, false);
        getActivity().setTitle("Cell Information");

        return rootView;
    }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Notification that 
		Log.i(TAG, "onCreate");
	}

	public void onStart() {
		super.onStart();	
		Log.i(TAG, "onStart");
	}
	
	public void onresume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}
	
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
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
		// Notification that the containing activiy and its View hierarchy exist
		Log.i(TAG, "onActivityCreated");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfiguration) {
		super.onConfigurationChanged(newConfiguration);
		
		Log.i(TAG, "onConfigurationChanged");
	}
	
	@Override
	public void onLowMemory() {
		// No guarantee this is called before or after other callbacks
		Log.i(TAG, "onLowMemory");
	}
}
