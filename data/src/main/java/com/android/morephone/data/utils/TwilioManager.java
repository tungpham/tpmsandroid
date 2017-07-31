package com.android.morephone.data.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.morephone.data.log.DebugTool;

public class TwilioManager {

    private static final String PREFERENCES_NAME = "twilio";
    private final static String SID = "sid";
    private final static String AUTH_CODE = "auth_code";
    private final static String APPLICATION_SID = "application_sid";

    public static void saveTwilio(Context context, String sid, String authCode) {
        DebugTool.logD("SAVE TWILIO: " + sid);
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit()
                .putString(SID, sid)
                .putString(AUTH_CODE, authCode)
                .commit();
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

    public static String getApplicationSid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(APPLICATION_SID, "");
    }

    public static void setApplicationSid(Context context, String applicationSid) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(APPLICATION_SID, applicationSid).commit();
    }


}
