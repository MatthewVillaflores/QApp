package com.tech.ivant.qapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.constants.StaticMethods;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.dao.ReportDao;
import android.widget.TextView;
import com.tech.ivant.qapp.constants.StaticMethods;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.entities.Report;
import com.tech.ivant.qapp.fragments.MonitorQueueFragment;
import com.tech.ivant.qapp.fragments.ReportFragment;
import com.tech.ivant.qapp.fragments.SettingsFragment;
import com.tech.ivant.qapp.fragments.SmsFragment;
import com.tech.ivant.qapp.fragments.ViewServiceFragment;
import com.tech.ivant.qapp.fragments.ViewServiceTopFragment;
import com.tech.ivant.qapp.fragments.fragment_navigation_drawer;

import java.text.SimpleDateFormat;


public class MainActivity extends ActionBarActivity  {
//
    public final static String SERVICE_ID_EXTRA = "com.tech.ivant.service_id";
    public final static String BROADCAST_AUTOMATIC_CLEAN_ALARM = "com.tech.ivant.alarm_automatic_clean";
    private final static String STATE_CURRENT_POSITION = "com.tech.ivant.current_fragment_position";

//    private ListView mDrawerList;
//    private ArrayAdapter<String> mAdapter;
//    private DrawerLayout mDrawerLayout;
//    private String mActivityTitle;
//    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentPosition;
    private Dialog addQueueDialog;
    private Dialog callNextDialog;
    public MonitorQueueFragment monitorQueueFragment;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_POSITION);
        } else {
            mCurrentPosition = 0;
        }
        setContentView(R.layout.activity_main);
        initializeApp();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragment_navigation_drawer drawerFragment = (fragment_navigation_drawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        monitorQueueFragment = new MonitorQueueFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.monitorqueue_container, monitorQueueFragment).commit();
//        mDrawerList = (ListView)findViewById(R.id.navigationDrawer);
//        fragment_navigation_drawer drawerFragment = (fragment_navigation_drawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//        String[] drawer_list = {
//                getString(R.string.nav_monitor_queue_title),
//                getString(R.string.nav_sms_title),
//                getString(R.string.nav_reports_title),
//                getString(R.string.nav_settings_title)};
//
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawer_list);
//        mDrawerList.setAdapter(mAdapter);
////
////
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mActivityTitle = getTitle().toString();
//        setUpDrawer();
//
//        Fragment home_fragment = new MonitorQueueFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.monitorqueue_container, home_fragment).commit();
//////
//        selectItem(mCurrentPosition);
//
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(position);
//            }
//        });
    }
//
    private void initializeApp(){
        DBManager.initializeDB(this);
        StaticMethods.mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        StaticMethods.mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        StaticMethods.context = this;
        StaticMethods.setUpAutomaticCleanAlarm();
        Service.createNoShowService();
    }
//
//    public void selectItem(int position){
//        Fragment fragment;
//        switch(position){
//            case 1:
//                fragment = new SmsFragment();
//                break;
//            case 2:
//                fragment = new ReportFragment();
//                break;
//            case 3:
//                fragment = new SettingsFragment();
//                break;
//            default:
//                fragment = new MonitorQueueFragment();
//                break;
//        }
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.monitorqueue_container, fragment).commit();
//        mCurrentPosition = position;
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
//
////
//    private void setUpDrawer(){
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.string.drawer_open, R.string.drawer_close) {
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//            }
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//            }
//        };
//
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//    }
//
//
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

        int id = item.getItemId();
        final View view = new View(this);
        final ViewServiceFragment fragmentBottom = monitorQueueFragment.getFragmentBottom();
        //noinspection SimplifiableIfStatement
        if (id == R.id.addQueue_toolbar) {
            Log.d(Constants.LOG_TAG, "addQueuefromtoolbar");
            addQueueDialog = new Dialog(view.getContext());
            addQueueDialog.setTitle("Add Queue - ");
            addQueueDialog.setContentView(R.layout.dialog_add_queue);
            TextView currentDate = (TextView) addQueueDialog.findViewById(R.id.addQueueCurrentDate);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            String preferredDateFormat = sharedPreferences.getString(view.getResources().getString(R.string.KEY_PREFERENCE_DATE_FORMAT), "MM/dd/yy");
            SimpleDateFormat dateFormat = new SimpleDateFormat(preferredDateFormat);
            currentDate.setText(dateFormat.format(System.currentTimeMillis()));

            Button cancel = (Button) addQueueDialog.findViewById(R.id.addQueueCancelButton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addQueueDialog.dismiss();
                }
            });

            Button add = (Button) addQueueDialog.findViewById(R.id.addQueueAddButton);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextName = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextName);
                    EditText editTextMobileNumber = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextMobileNumber);
                    EditText editTextNotes = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextNotes);

                    String customerName = editTextName.getText().toString();
                    String mobileNumber = editTextMobileNumber.getText().toString();
                    String notes = editTextNotes.getText().toString();

                    long service_id = fragmentBottom.getmService().id;

                    Queue queue = Queue.enqueue(customerName, mobileNumber, notes, service_id);
                    fragmentBottom.updateList(view);
                    addQueueDialog.dismiss();
                }
            });
            addQueueDialog.show();
            return true;
        }

        if (id == R.id.callNext_toolbar) {
            callNextDialog = new Dialog(new View(this).getContext());
            callNextDialog.setContentView(R.layout.dialog_call_next);
            TextView customerName = (TextView) callNextDialog.findViewById(R.id.dialogCallNextCustomerName);

            TextView arrivalTime = (TextView) callNextDialog.findViewById(R.id.dialogCallNextArrivedTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            TextView mobileNumber = (TextView) callNextDialog.findViewById(R.id.dialogCallNextMobileNumber);
            TextView notes = (TextView) callNextDialog.findViewById(R.id.dialogCallNextNotes);

            Button cancelButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextCancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callNextDialog.dismiss();
                }
            });

            Button noShowButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextNoShowButton);
            Button arrivedButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextArrivedButton);
//            arrivedButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mService.startNumber++;
//                    ServiceDao.update(mService);
//                    Queue.call(cCalled);
//                    updateList(v);
//                    callNextDialog.dismiss();
//                }
//            });
            callNextDialog.show();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_CURRENT_POSITION, mCurrentPosition);

        super.onSaveInstanceState(savedInstanceState);
    }
////
//    private Dialog newServiceDialog;
//
//    public void newService(View view){
//
//        //newServiceDialog = new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.dialog_new_service, null)).show();
//        newServiceDialog = new Dialog(this);
//        newServiceDialog.setTitle("New Service");
//        newServiceDialog.setContentView(R.layout.dialog_new_service);
//        newServiceDialog.show();
//
//        Button cancel_button = (Button) newServiceDialog.findViewById(R.id.dialog_new_service_cancel_button);
//        cancel_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                newServiceDialog.dismiss();
//            }
//        });
//
//        Button create_button = (Button) newServiceDialog.findViewById(R.id.dialog_new_service_create_button);
//        create_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createNewService(v);
//            }
//        });
        /*
        Intent intent = new Intent(this, NewService.class);
        startActivity(intent);
        */
//    }

//    public void cancelNewService(View view){
//        if(newServiceDialog != null){
//            newServiceDialog.dismiss();
//        }
//    }
//
//    public void createNewService(View view){
//        EditText service_name_edit = (EditText) newServiceDialog.findViewById(R.id.dialog_new_service_name);
//        if(service_name_edit != null) {
//            Service service = new Service(service_name_edit.getText().toString(), "");
//            ServiceDao.save(service);
//        }
//        newServiceDialog.dismiss();
////        selectItem(mCurrentPosition);
//
//    }
}
