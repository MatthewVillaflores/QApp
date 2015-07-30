package com.tech.ivant.qapp.constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.preferences.TimePickerPreference;
import com.tech.ivant.qapp.receiver.CleanUpReceiver;
import com.tech.ivant.qapp.util.TimeHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by matthew on 7/22/15.
 */
public class StaticMethods {
    private static final String LOG_KEY = "STATIC_METHODS";

    public static AlarmManager mAlarmManager;
    public static SharedPreferences mSharedPreference;
    public static Context context;

    public static void setUpAutomaticCleanAlarm(){

        SharedPreferences sharedPreferences = mSharedPreference;
        if(sharedPreferences.getBoolean(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN), false)){


            Intent alarmIntent = new Intent(context, CleanUpReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = mAlarmManager;
            alarmManager.cancel(pendingIntent);

            TimeHandler timeHandler = new TimeHandler(sharedPreferences.getLong(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN_TIME), TimePickerPreference.DEFAULT_VALUE));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, timeHandler.getHOUR());
            calendar.set(Calendar.MINUTE, timeHandler.getMINUTE());

            Log.d(Constants.LOG_TAG, "Setting up Alarm " + calendar.get(Calendar.HOUR_OF_DAY) +"h" + calendar.get(Calendar.MINUTE)+"m");
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
