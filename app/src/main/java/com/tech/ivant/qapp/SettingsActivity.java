package com.tech.ivant.qapp;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.fragments.MonitorQueueFragment;
import com.tech.ivant.qapp.fragments.*;
import com.tech.ivant.qapp.fragments.fragment_navigation_drawer;


public class SettingsActivity extends ActionBarActivity {

    private SettingsFragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");
        fragment_navigation_drawer drawerFragment = (fragment_navigation_drawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout_settings), toolbar);

        mSettingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.settingsfragment_container, mSettingsFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Dialog newServiceDialog;

    public void newService(View view){

        //newServiceDialog = new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.dialog_new_service, null)).show();
        newServiceDialog = new Dialog(this);
        newServiceDialog.setTitle("New Service");
        newServiceDialog.setContentView(R.layout.dialog_new_service);
        newServiceDialog.show();

        Button cancel_button = (Button) newServiceDialog.findViewById(R.id.dialog_edit_service_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                newServiceDialog.dismiss();
            }
        });

        Button create_button = (Button) newServiceDialog.findViewById(R.id.dialog_edit_service_save_button);
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

    public void createNewService(View view){
        EditText service_name_edit = (EditText) newServiceDialog.findViewById(R.id.dialog_edit_service_name);
        if(service_name_edit != null) {
            Log.d(Constants.LOG_TAG, "Service edit text name is not null");
            Service service = new Service(service_name_edit.getText().toString(), "");
            ServiceDao.save(service);
            mSettingsFragment.updateServicesList();
        }
        newServiceDialog.dismiss();
//        selectItem(mCurrentPosition);

    }
}
