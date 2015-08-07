package com.tech.ivant.qapp.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.adapters.QueueAdapter;
import com.tech.ivant.qapp.receiver.CleanUpReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by matthew on 7/15/15.
 */
public class ViewServiceFragment extends Fragment{
    public final static String LOG_TAG = "VewServiceFragment";

    public Service mService;

    public Service getmService() {
        return mService;
    }

    private Dialog addQueueDialog;
    private Dialog callNextDialog;
    private ArrayAdapter<String> mAdapter;
    private QueueAdapter qAdapter;
    private BaseAdapter bAdapter;
    private ListView mListView;

    public Queue[] getQueueList() {
        return queueList;
    }

    public Queue[] queueList;
    private BroadcastReceiver mCleanupBroadCastListener;
    private SharedPreferences mSharedPreference;

    public ViewServiceFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_view_service, container, false);

        Bundle arguments = getArguments();
        mService  = ServiceDao.find(arguments.getLong(MonitorQueueFragment.KEY_SERVICE_ID));

        mListView = (ListView) rootView.findViewById(R.id.queueListView);

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        boolean canManualCall = mSharedPreference.getBoolean(rootView.getResources().getString(R.string.KEY_PREFERENCE_MANUAL_CALL), true);
        if(canManualCall){
            mListView.setOnItemClickListener(new QueueListListener());
        }

        Button newQueue = (Button) rootView.findViewById(R.id.addQueueButton);
        NewQueueListener newqueuelistener = new NewQueueListener();
        newQueue.setOnClickListener(newqueuelistener);

        Button callNext = (Button) rootView.findViewById(R.id.callNextButton);
        CallNextListener callnextlistener = new CallNextListener();
        callNext.setOnClickListener(callnextlistener);

        mCleanupBroadCastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getBooleanExtra(CleanUpReceiver.EXTRA_QUEUE_CLEANED, true)){
                    updateList(rootView);
                }
            }
        };

        LocalBroadcastManager.getInstance(rootView.getContext()).registerReceiver(mCleanupBroadCastListener, new IntentFilter("queue_clean_up"));

        updateList(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mCleanupBroadCastListener);
        super.onDestroy();
    }

    public void updateList(View rootView){

        queueList = QueueDao.where(QueueDao.QueueEntry.COLUMN_NAME_SERVICE_ID, mService.id);

        if(queueList.length > 0) {
            bAdapter = new QueueAdapter(getActivity(), new ArrayList<Queue>(Arrays.asList(queueList)), rootView.getResources());
        }else{
            String[] content = {"Nothing to display"};
            bAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, content);
        }
        mListView.setAdapter(bAdapter);
        /*
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);


        queueList = Queue.where(getActivity(), Queue.QueueEntry.COLUMN_NAME_SERVICE_ID, mService.id + "");

        if(queueList != null) {
            for (Queue queue : queueList) {
                mAdapter.add(queue.customerName);
            }
        } else {
            mAdapter.add("Nothing to display");
        }
        mListView.setAdapter(mAdapter);
        */
        broadcastDbChange();
    }

    public void broadcastDbChange(){
        Intent localBroadcast = new Intent("db_new_queue");
        localBroadcast.putExtra(MonitorQueueFragment.EXTRA_NEW_QUEUE, true);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(localBroadcast);
    }

    private class NewQueueListener implements View.OnClickListener{

        public NewQueueListener(){
        }

        @Override
        public void onClick(View v) {
            addQueueDialog = new Dialog(v.getContext());
            addQueueDialog.setTitle("Add Queue - " + mService.name);
            addQueueDialog.setContentView(R.layout.dialog_add_queue);

            TextView currentDate = (TextView) addQueueDialog.findViewById(R.id.addQueueCurrentDate);
            String preferredDateFormat = mSharedPreference.getString(v.getResources().getString(R.string.KEY_PREFERENCE_DATE_FORMAT), "MM/dd/yy");
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
            add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    addQueue(v);
                }
            });

            addQueueDialog.show();
        }

        public void addQueue(View v){
            EditText editTextName = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextName);
            EditText editTextMobileNumber = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextMobileNumber);
            EditText editTextNotes = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextNotes);

            String customerName = editTextName.getText().toString();
            String mobileNumber = editTextMobileNumber.getText().toString();
            String notes = editTextNotes.getText().toString();
            long service_id = mService.id;

            Queue queue = Queue.enqueue(customerName, mobileNumber, notes, service_id);

            Log.d(LOG_TAG, "Added new Queue: " + queue.id + ":" + queue.customerName + ":" + queue.mobileNumber + ":" + queue.notes + ":" + queue.service_id);
            Log.d(LOG_TAG, "To Service: " + mService.name + ":" + mService.id);
            addQueueDialog.dismiss();

            updateList(v);
        }
    }

    private class CallNextListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(queueList != null && queueList.length >0) {

                Queue called = null;
                for(Queue queue : queueList){
                    if(queue.queueNumber == mService.startNumber){
                        called = queue;
                        break;
                    }
                }
                if(called == null && queueList.length != 0){
                    called = queueList[0];
                    mService.startNumber = called.queueNumber;
                }

                showCallNextDialog(called, v.getContext());
            } else {
                CharSequence noQueueMessage = "No Queue to be Called";
                int duration = Toast.LENGTH_SHORT;

                Toast noQueueToast = Toast.makeText(v.getContext(), noQueueMessage, duration);
                noQueueToast.show();

            }
        }
    }

    private class QueueListListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Queue clickedQueue = queueList[position];

            showCallNextDialog(clickedQueue, view.getContext());

        }
    }

    private void showCallNextDialog(Queue called, Context context){

        callNextDialog = new Dialog(context);
        callNextDialog.setContentView(R.layout.dialog_call_next);
        callNextDialog.setTitle("Next Customer - " + mService.name);

        TextView customerName = (TextView) callNextDialog.findViewById(R.id.dialogCallNextCustomerName);
        customerName.setText(called.customerName);

        TextView arrivalTime = (TextView) callNextDialog.findViewById(R.id.dialogCallNextArrivedTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        arrivalTime.setText(dateFormat.format(called.queueDate));

        TextView mobileNumber = (TextView) callNextDialog.findViewById(R.id.dialogCallNextMobileNumber);
        mobileNumber.setText(called.mobileNumber);

        TextView notes = (TextView) callNextDialog.findViewById(R.id.dialogCallNextNotes);
        notes.setText(called.notes);

        Button cancelButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextDialog.dismiss();
            }
        });

        Button noShowButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextNoShowButton);

        final Queue cCalled = called;

        Button arrivedButton = (Button) callNextDialog.findViewById(R.id.dialogCallNextArrivedButton);
        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.startNumber++;
                ServiceDao.update(mService);
                Queue.call(cCalled, mService);
                updateList(v);
                callNextDialog.dismiss();
            }
        });


        callNextDialog.show();
    }
}
