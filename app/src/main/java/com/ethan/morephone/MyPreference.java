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

    private static final String PROPERTY_INBOX = "PROPERTY_INBOX";

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

    public static boolean getInbox(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_INBOX, true);
    }

    public static void setInbox(Context context, boolean isInbox){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_INBOX, isInbox).commit();
    }
}
