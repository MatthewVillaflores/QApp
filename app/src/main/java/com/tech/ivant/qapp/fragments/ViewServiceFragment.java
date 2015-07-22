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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tech.ivant.qapp.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.Service;
import com.tech.ivant.qapp.adapters.QueueAdapter;
import com.tech.ivant.qapp.receiver.AlarmBroadcastReceiver;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matthew on 7/15/15.
 */
public class ViewServiceFragment extends Fragment{
    public final static String LOG_TAG = "VewServiceFragment";

    private Service mService;
    private Dialog addQueueDialog;
    private Dialog callNextDialog;
    private ArrayAdapter<String> mAdapter;
    private QueueAdapter qAdapter;
    private BaseAdapter bAdapter;
    private ListView mListView;
    private Queue[] queueList;
    private BroadcastReceiver mCleanupBroadCastListener;

    public ViewServiceFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_view_service, container, false);

        Bundle arguments = getArguments();
        mService  = Service.find(getActivity(), arguments.getLong(MonitorQueueFragment.KEY_SERVICE_ID));

        mListView = (ListView) rootView.findViewById(R.id.queueListView);

        Button newQueue = (Button) rootView.findViewById(R.id.addQueueButton);
        NewQueueListener newqueuelistener = new NewQueueListener();
        newQueue.setOnClickListener(newqueuelistener);

        Button callNext = (Button) rootView.findViewById(R.id.callNextButton);
        CallNextListener callnextlistener = new CallNextListener();
        callNext.setOnClickListener(callnextlistener);

        mCleanupBroadCastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_QUEUE_CLEANED, true)){
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

        queueList = Queue.where(getActivity(), Queue.QueueEntry.COLUMN_NAME_SERVICE_ID, mService.id + "");

        if(queueList != null) {
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            String preferredDateFormat = sharedPreferences.getString(v.getResources().getString(R.string.KEY_PREFERENCE_DATE_FORMAT), "MM/dd/yy");
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
            Queue queue = new Queue();
            EditText editTextName = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextName);
            EditText editTextMobileNumber = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextMobileNumber);
            EditText editTextNotes = (EditText) addQueueDialog.findViewById(R.id.addQueueEditTextNotes);

            queue.customerName = editTextName.getText().toString();
            queue.mobileNumber = editTextMobileNumber.getText().toString();
            queue.notes = editTextNotes.getText().toString();
            queue.queueDate = System.currentTimeMillis();
            queue.service_id = mService.id;

            mService.endNumber++;
            queue.queueNumber = mService.endNumber;

            queue.save(v.getContext());
            mService.update(v.getContext());

            Log.d(LOG_TAG, "Added new Queue: " + queue.id + ":" + queue.customerName + ":" + queue.mobileNumber + ":" + queue.notes + ":" + queue.service_id);
            Log.d(LOG_TAG, "To Service: " + mService.name + ":" + mService.id);
            addQueueDialog.dismiss();

            updateList(v);
        }
    }

    private class CallNextListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(queueList != null) {
                callNextDialog = new Dialog(v.getContext());
                callNextDialog.setContentView(R.layout.dialog_call_next);
                callNextDialog.setTitle("Next Customer - " + mService.name);

                Queue called = null;
                for(Queue queue : queueList){
                    if(queue.queueNumber == mService.startNumber){
                        called = queue;
                        break;
                    }
                }
                if(called == null){
                    called = queueList[0];
                    mService.startNumber = called.queueNumber;
                }

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
                        cCalled.delete(v.getContext());
                        updateList(v);
                        callNextDialog.dismiss();
                    }
                });


                callNextDialog.show();
            }
        }
    }
}
