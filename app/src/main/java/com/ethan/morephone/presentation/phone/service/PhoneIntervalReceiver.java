package com.ethan.morephone.presentation.phone.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Ethan on 4/27/17.
 */

public class PhoneIntervalReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, PhoneService.class);
        startWakefulService(context, service);
    }
}
