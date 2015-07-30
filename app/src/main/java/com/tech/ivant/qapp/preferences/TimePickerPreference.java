package com.tech.ivant.qapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.util.TimeHandler;

import java.text.SimpleDateFormat;

/**
 * Created by matthew on 7/21/15.
 */
public class TimePickerPreference extends DialogPreference {

    public static final long DEFAULT_VALUE = 57600000;

    private final String LOG_KEY = "TIME_PICKER_PREFERENCE";

    private TimePicker mTimePicker;
    private TimeHandler mCurrentValue;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("Ok");
        setNegativeButtonText("Cancel");

        mCurrentValue = new TimeHandler();
        changeValue(this.getPersistedLong(DEFAULT_VALUE));

        setDialogIcon(null);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d(LOG_KEY, "from shared preference: " + sharedPreferences.getLong(context.getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN_TIME), DEFAULT_VALUE));

    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue){
            changeValue(this.getPersistedLong(DEFAULT_VALUE));
        }else{
            persistLong(mCurrentValue.getLongValue());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return super.onGetDefaultValue(a, index);
    }

    @Override
    protected View onCreateDialogView() {
        mTimePicker = new TimePicker(getContext());
        return mTimePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Log.d(LOG_KEY, "set time " + dateFormat.format(mCurrentValue.getLongValue()) + ":" + mCurrentValue.getLongValue());

        long hourvalue = mCurrentValue.getHOUR();
        long minutevalue = mCurrentValue.getMINUTE();
        Log.d(LOG_KEY, "output time" + hourvalue +"h "+ minutevalue +"m");
        mTimePicker.setCurrentHour( (int) hourvalue );
        mTimePicker.setCurrentMinute( (int) minutevalue );

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult) {
            if (shouldPersist()) {
                changeValue(mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
                persistLong(mCurrentValue.getLongValue());
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                Log.d(LOG_KEY, "saved time: " + dateFormat.format(mCurrentValue.getLongValue()) + ":" + mCurrentValue.getLongValue());
            } else {
                Log.w(LOG_KEY, "preference not saved");
            }
        }
    }

    private void changeValue(int hour, int minute){
        mCurrentValue.setHOUR(hour);
        mCurrentValue.setMINUTE(minute);
    }

    private void changeValue(long value){
        mCurrentValue.setLongValue(value);
    }
}
