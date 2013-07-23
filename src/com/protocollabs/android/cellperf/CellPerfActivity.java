package com.protocollabs.android.cellperf;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.content.LocalBroadcastManager;

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
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;



public class CellPerfActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCategories;

    private Bundle mBundle;

    /* Service and Broadcast specific */
    private MeasurementsService mMeasurmentsExecuter;
    private DataUpdateReceiver dataUpdateReceiver;

    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mCategories = getResources().getStringArray(R.array.app_subcomponents);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        mBundle = new Bundle();
        mBundle.putString("unique-id", Utils.getUniqueId(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");

        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu");

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fm;
        Bundle args = new Bundle();
        Fragment fragment;
        FragmentTransaction ft;

         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        fm = getFragmentManager();


        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_settings:
                // FIXME: this fragment must be stacked on top
                // of the current active fragment to let the back
                // button work      -HGN
                fragment = fm.findFragmentByTag("SettingsFragment");
                if (fragment == null) {
                    fragment = new SettingsFragment();
                    fragment.setArguments(args);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment, "SettingsFragment");
                    ft.commit();
                } else {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                setTitle("Settings");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment;
        FragmentManager fm;
        FragmentTransaction ft;
        Bundle args;
        
        args = new Bundle();
        fm = getFragmentManager();

        switch (position) {

            case 0:
                fragment = fm.findFragmentByTag("CellInformationFragment");
                if (fragment == null) {
                    fragment = new CellInformationFragment();
                    fragment.setArguments(args);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment, "CellInformationFragment");
                    ft.commit();
                } else {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                break;

            case 1:
                fragment = fm.findFragmentByTag("MeasurementFragment");
                if (fragment == null) {
                    fragment = new MeasurementFragment();
                    fragment.setArguments(args);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment, "MeasurementFragment");
                    ft.commit();
                } else {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                break;

            case 2:
                fragment = fm.findFragmentByTag("AnalysisMapFragment");
                if (fragment == null) {
                    fragment = new AnalysisMapFragment();
                    fragment.setArguments(args);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment, "AnalysisMapFragment");
                    ft.commit();
                } else {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                break;

            case 3:
                fragment = fm.findFragmentByTag("BacklogFragment");
                if (fragment == null) {
                    fragment = new BacklogFragment();
                    fragment.setArguments(args);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment, "BacklogFragment");
                    ft.commit();
                } else {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                break;

            default:
                Toast.makeText(this, R.string.programmed_error_warning, Toast.LENGTH_LONG).show();
                break;
        }


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mCategories[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onPostCreate");
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter("measurement-data-ready");
        registerReceiver(dataUpdateReceiver, intentFilter);

        bindService(new Intent(this, MeasurementsService.class), mConnection, Context.BIND_AUTO_CREATE);
    }



    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
        unbindService(mConnection);
    }


    public Bundle getBundle() {
        return mBundle;
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            MeasurementsService.MeasurementsBinder localBinder = (MeasurementsService.MeasurementsBinder) binder;
            mMeasurmentsExecuter = localBinder.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mMeasurmentsExecuter = null;
        }
    };

    public void showData() {
        Log.i(TAG, "showData");
        if (mMeasurmentsExecuter != null) {
            mMeasurmentsExecuter.getMeasurementsResults();
        }
    }


    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("ping-measurement");
            Log.d("receiver", "Got message: " + message);
        }

    }

}
