package com.tech.ivant.qapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    public final static String SERVICE_ID_EXTRA = "com.tech.ivant.service_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManager.initializeDB(this);

    }

    @Override
    public void onResume(){
        super.onResume();

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
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent open_service = new Intent(context, ViewService.class);
                open_service.putExtra(SERVICE_ID_EXTRA, services[position].id+"");
                startActivity(open_service);
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newService(View view){
        Intent intent = new Intent(this, NewService.class);
        startActivity(intent);
    }
}
