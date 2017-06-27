package com.android.morephone.data.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TwilioManager {

    private static final String PREFERENCES_NAME = "twilio";
    private final static String SID = "sid";
    private final static String AUTH_CODE = "auth_code";

    public static void saveTwilio(Context context, String sid, String authCode) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit()
                .putString(SID, sid)
                .putString(AUTH_CODE, authCode)
                .apply();
    }

    public static String getSid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getString(SID, null);

    }

    public static String getAuthCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getString(AUTH_CODE, null);

    }


}
