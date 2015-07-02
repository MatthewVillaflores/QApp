package com.tech.ivant.qapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity{

    public final static String SERVICE_ID_EXTRA = "com.tech.ivant.service_id";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Fragment home_fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, home_fragment).commit();

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
                fragment = new HomeFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();

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
        super.onResume();
/*
        final Service[] services = Service.all(this);

        final ListView servicesList = (ListView) findViewById(R.id.serviceListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        servicesList.setAdapter(adapter);

        if( services != null) {
            for (Service service : services) {
                adapter.add(service.name);
            }
        }

        final MainActivity context = this;

        servicesList.setClickable(true);
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent open_service = new Intent(context, ViewService.class);
                open_service.putExtra(SERVICE_ID_EXTRA, services[position].id + "");
                startActivity(open_service);
            }
        });
*/
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //if(!mNavigationDrawerFragment.isDrawerOpen()){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        //    restoreActionBar();
        //    return true;
        //}
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

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


    public void newService(View view){
        Intent intent = new Intent(this, NewService.class);
        startActivity(intent);
    }

}
