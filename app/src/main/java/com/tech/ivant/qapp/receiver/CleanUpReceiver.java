package com.tech.ivant.qapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.preferences.TimePickerPreference;
import com.tech.ivant.qapp.util.TimeHandler;

/**
 * Created by matthew on 7/22/15.
 */
public class CleanUpReceiver extends BroadcastReceiver {
    private final String LOG_KEY = "ALARM_RECEIVER";
    public static final String EXTRA_QUEUE_CLEANED = "com.tech.ivant.clean_up_time";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        TimeHandler cleanTime = new TimeHandler(sharedPreferences.getLong(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN_TIME), TimePickerPreference.DEFAULT_VALUE));
        TimeHandler currentTime = new TimeHandler(System.currentTimeMillis());
        Log.d(Constants.LOG_TAG, "Alarm fire off");
        Log.d(Constants.LOG_TAG, "cleantime: " + cleanTime.getHOUR() +"h"+cleanTime.getMINUTE()+"m"
                + "currenttime: " +currentTime.getHOUR() +"h"+currentTime.getMINUTE()+"m");


        if (sharedPreferences.getBoolean(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN), false)) {
            Log.d(Constants.LOG_TAG, "Hello im here. Notice me senpai");
            if(cleanTime.getHOUR()==currentTime.getHOUR() && cleanTime.getMINUTE() == currentTime.getMINUTE()) {
                Log.d(Constants.LOG_TAG, "Cleaning up queue");
                Service[] services = ServiceDao.all();
                if (services != null) {
                    for (Service service : services) {
                        Queue[] queues = QueueDao.where(QueueDao.QueueEntry.COLUMN_NAME_SERVICE_ID, service.id + "");
                        for (Queue queue : queues) {
                            QueueDao.delete(queue);
                        }
                    }
                    broadcastClean(context);
                }
            }
        }
    }

    private void broadcastClean(Context context){
        Intent localBroadcast = new Intent("queue_clean_up");
        localBroadcast.putExtra(EXTRA_QUEUE_CLEANED, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localBroadcast);
    }
}
