package com.tech.ivant.qapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Service;


public class NewQueue extends ActionBarActivity {

    long service_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queue);
        Intent intent = getIntent();
        service_id = Long.parseLong(intent.getStringExtra(MainActivity.SERVICE_ID_EXTRA));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_queue, menu);
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

    public void newQueue(View view){
        EditText edit_name = (EditText) findViewById(R.id.queue_name_edit_text);
        EditText edit_note = (EditText) findViewById(R.id.queue_notes_edit_text);
        EditText edit_mobile = (EditText) findViewById(R.id.queue_mobile_edit_text);

        Queue new_q = new Queue(edit_name.getText().toString(), edit_note.getText().toString(), edit_mobile.getText().toString(), service_id);
        new_q.save(this);
        Service service = ServiceDao.find(service_id);
        service.endNumber++;
        new_q.queueNumber = service.endNumber;
        new_q.update(this);
        ServiceDao.update(service);
        finish();
    }
}
