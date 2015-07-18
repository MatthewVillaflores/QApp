package com.tech.ivant.qapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.tech.ivant.qapp.fragments.MonitorQueueFragment;
import com.tech.ivant.qapp.fragments.ReportFragment;
import com.tech.ivant.qapp.fragments.SettingsFragment;
import com.tech.ivant.qapp.fragments.SmsFragment;


public class MainActivity extends ActionBarActivity {

    public final static String SERVICE_ID_EXTRA = "com.tech.ivant.service_id";
    private final static String STATE_CURRENT_POSITION = "com.tech.ivant.current_fragment_position";

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_POSITION);
        }else{
            mCurrentPosition = 0;
        }

        setContentView(R.layout.activity_main);

        DBManager.initializeDB(this);

        mDrawerList = (ListView)findViewById(R.id.navigationDrawer);
        String[] drawer_list = {
                getString(R.string.nav_monitor_queue_title),
                getString(R.string.nav_sms_title),
                getString(R.string.nav_reports_title),
                getString(R.string.nav_settings_title)};

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawer_list);
        mDrawerList.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActivityTitle = getTitle().toString();
        setUpDrawer();

        Fragment home_fragment = new MonitorQueueFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, home_fragment).commit();

        selectItem(mCurrentPosition);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

    }

    public void selectItem(int position){
        Fragment fragment;
        switch(position){
            case 1:
                fragment = new SmsFragment();
                break;
            case 2:
                fragment = new ReportFragment();
                break;
            case 3:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new MonitorQueueFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
        mCurrentPosition = position;
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    private void setUpDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void onResume(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String company_name = sharedPreferences.getString(getResources().getString(R.string.KEY_PREFERENCE_COMPANY_NAME), "Company ABC");

        this.setTitle(company_name);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

        */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_CURRENT_POSITION, mCurrentPosition);

        super.onSaveInstanceState(savedInstanceState);
    }

    private Dialog newServiceDialog;

    public void newService(View view){

        //newServiceDialog = new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.dialog_new_service, null)).show();
        newServiceDialog = new Dialog(this);
        newServiceDialog.setTitle("New Service");
        newServiceDialog.setContentView(R.layout.dialog_new_service);
        newServiceDialog.show();

        Button cancel_button = (Button) newServiceDialog.findViewById(R.id.dialog_new_service_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                newServiceDialog.dismiss();
            }
        });

        Button create_button = (Button) newServiceDialog.findViewById(R.id.dialog_new_service_create_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewService(v);
            }
        });
        /*
        Intent intent = new Intent(this, NewService.class);
        startActivity(intent);
        */
    }

    public void cancelNewService(View view){
        if(newServiceDialog != null){
            newServiceDialog.dismiss();
        }
    }

    public void createNewService(View view){
        EditText service_name_edit = (EditText) newServiceDialog.findViewById(R.id.dialog_new_service_name);
        if(service_name_edit != null) {
            Service service = new Service(service_name_edit.getText().toString(), "");
            service.save(this);
        }
        newServiceDialog.dismiss();
        selectItem(mCurrentPosition);

    }
}
