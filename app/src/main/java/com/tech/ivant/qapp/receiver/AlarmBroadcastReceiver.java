package com.tech.ivant.qapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Service;

/**
 * Created by matthew on 7/22/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_KEY = "ALARM_RECEIVER";
    public static final String EXTRA_QUEUE_CLEANED = "com.tech.ivant.clean_up_time";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN), false)) {
            Log.d(LOG_KEY, "Cleaning up queue");
            Service[] services = ServiceDao.all();
            if( services != null ) {
                for (Service service : services) {
                    Queue[] queues = Queue.where(context, Queue.QueueEntry.COLUMN_NAME_SERVICE_ID, service.id + "");
                    if (queues != null) {
                        for (Queue queue : queues) {
                            queue.delete(context);
                        }
                    }
                }
                broadcastClean(context);
            }
        }
    }

    private void broadcastClean(Context context){
        Intent localBroadcast = new Intent("queue_clean_up");
        localBroadcast.putExtra(EXTRA_QUEUE_CLEANED, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localBroadcast);
    }
}
