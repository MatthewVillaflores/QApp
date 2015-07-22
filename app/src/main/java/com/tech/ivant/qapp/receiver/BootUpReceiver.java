package com.tech.ivant.qapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tech.ivant.qapp.constants.StaticMethods;

/**
 * Created by matthew on 7/22/15.
 */
public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            StaticMethods.setUpAutomaticCleanAlarm(context);
        }
    }
}
