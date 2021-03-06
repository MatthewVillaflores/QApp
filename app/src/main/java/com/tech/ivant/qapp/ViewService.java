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
import android.widget.TextView;

import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.entities.Service;

import java.text.SimpleDateFormat;


public class ViewService extends ActionBarActivity {

    private Service service;
    private Queue[] queue_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);

    }

    @Override
    public void onResume(){
        super.onResume();
        
        TextView service_name = (TextView) findViewById(R.id.serviceName);
        TextView service_note = (TextView) findViewById(R.id.serviceNote);

        Intent intent = getIntent();
        long id = Long.parseLong(intent.getStringExtra(MainActivity.SERVICE_ID_EXTRA));
        service = ServiceDao.find(id);
        if(service != null) {
            service_name.setText(service.name);
            service_note.setText(service.notes);
        }

        queue_list = QueueDao.where(QueueDao.QueueEntry.COLUMN_NAME_SERVICE_ID, service.id + "");

        final ListView queue_list_view = (ListView) findViewById(R.id.queueList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        queue_list_view.setAdapter(adapter);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");

        if(queue_list!=null){
            for(Queue queue : queue_list){
                adapter.add(df.format(queue.queueDate
                ) + " : " + queue.customerName);
            }
        }
        final ViewService context = this;
        queue_list_view.setClickable(true);
        queue_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                new AlertDialog.Builder(context)
                        .setMessage(queue_list[position].customerName + "\n"
                                + queue_list[position].mobileNumber + "\n"
                                + queue_list[position].notes)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
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

    public void newQueue(View view){
        Intent new_queue = new Intent(this, NewQueue.class);
        new_queue.putExtra(MainActivity.SERVICE_ID_EXTRA, service.id+"");
        startActivity(new_queue);
    }

    public void callNext(View view){
        Queue called = null;
        if(queue_list != null) {
            for (Queue queue : queue_list) {
                if (queue.queueNumber == service.startNumber) {
                    called = queue;
                    break;
                }
            }
        }
        if(called == null){
            new AlertDialog.Builder(this)
                    .setTitle("Call")
                    .setMessage("No one to call")
                    .show();
        }else{
            final Queue f_called = called;
            final Context f_context = this;
            new AlertDialog.Builder(this)
                    .setTitle("Call")
                    .setMessage(called.customerName + " is next")
                    .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            QueueDao.delete(f_called);
                            service.startNumber++;
                            ServiceDao.update(service);
                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

    }
}
