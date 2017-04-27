package com.ethan.morephone.presentation.phone.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ethan on 4/27/17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PhoneService.startPhoneService(context);
    }
}
