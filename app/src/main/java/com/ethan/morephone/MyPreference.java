package com.ethan.morephone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ethan on 2/25/17.
 */

public class MyPreference {

    private static final String PROPERTY_USER_EMAIL = "PROPERTY_USER_EMAIL";
    private static final String PROPERTY_USER_FIRST_NAME = "PROPERTY_USER_FIRST_NAME";
    private static final String PROPERTY_USER_LAST_NAME = "PROPERTY_USER_LAST_NAME";
    private static final String PROPERTY_USER_PASSWORD = "PROPERTY_USER_PASSWORD";

    private static final String PROPERTY_PHONE_NUMBER = "PROPERTY_PHONE_NUMBER";
    private static final String PROPERTY_FRIENDLY_NAME = "PROPERTY_FRIENDLY_NAME";
    private static final String PROPERTY_PHONE_NUMBER_SID = "PROPERTY_PHONE_NUMBER_SID";

    private static final String PROPERTY_INBOX = "PROPERTY_INBOX";

    private static final String PROPERTY_NOT_NOW = "PROPERTY_NOT_NOW";
    private static final String PROPERTY_RATE_NOW = "PROPERTY_RATE_NOW";
    private static final String PROPERTY_TIMES_USE = "PROPERTY_TIMES_USE";

    private static final String APP_PREF = "MorePhone";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static String getUserEmail(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_EMAIL, "");
    }

    public static void setUserEmail(Context context, String email){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_EMAIL, email).commit();
    }

    public static String getFirstName(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_FIRST_NAME, "");
    }

    public static void setFirstName(Context context, String firstName){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_FIRST_NAME, firstName).commit();
    }

    public static String getLastName(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_LAST_NAME, "");
    }

    public static void setLastName(Context context, String lastName){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_LAST_NAME, lastName).commit();
    }

    public static String getPassword(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_PASSWORD, "");
    }

    public static void setPassword(Context context, String password){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_PASSWORD, password).commit();
    }

    public static String getPhoneNumber(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_PHONE_NUMBER, "");
    }

    public static void setPhoneNumber(Context context, String phoneNumber){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_PHONE_NUMBER, phoneNumber).commit();
    }

    public static String getFriendlyName(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_FRIENDLY_NAME, "");
    }

    public static void setFriendlyName(Context context, String phoneNumber){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_FRIENDLY_NAME, phoneNumber).commit();
    }

    public static String getPhoneNumberSid(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_PHONE_NUMBER_SID, "");
    }

    public static void setPhoneNumberSid(Context context, String phoneNumber){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_PHONE_NUMBER_SID, phoneNumber).commit();
    }

    public static boolean getInbox(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_INBOX, true);
    }

    public static void setInbox(Context context, boolean isInbox){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_INBOX, isInbox).commit();
    }

    public static boolean getNotNow(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_NOT_NOW, false);
    }

    public static void setPrefNotNow(Context context, Boolean notNow) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_NOT_NOW, notNow).commit();
    }

    public static boolean getRateNow(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_RATE_NOW, false);
    }

    public static void setPrefRateNow(Context context, Boolean rateNow) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_RATE_NOW, rateNow).commit();
    }

    public static long getTimesUse(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getLong(PROPERTY_TIMES_USE, 0);
    }

    public static void setTimesUse(Context context, long timesUse){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putLong(PROPERTY_TIMES_USE, timesUse).commit();
    }
}
