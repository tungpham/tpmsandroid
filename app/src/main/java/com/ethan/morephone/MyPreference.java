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
    private static final String PROPERTY_USER_NAME = "PROPERTY_USER_NAME";
    private static final String PROPERTY_USER_GIVEN_NAME = "PROPERTY_USER_GIVEN_NAME";
    private static final String PROPERTY_USER_NICK_NAME = "PROPERTY_USER_NICK_NAME";
    private static final String PROPERTY_USER_PICTURE = "PROPERTY_USER_PICTURE";

    private static final String PROPERTY_PHONE_NUMBER = "PROPERTY_PHONE_NUMBER";
    private static final String PROPERTY_PHONE_FRIENDLY_NAME = "PROPERTY_PHONE_FRIENDLY_NAME";
    private static final String PROPERTY_PHONE_NUMBER_SID = "PROPERTY_PHONE_NUMBER_SID";

    private static final String PROPERTY_REGISTER_PHONE_NUMBER = "PROPERTY_REGISTER_PHONE_NUMBER";

    private static final String PROPERTY_SETTING_ENABLE_RECORD = "PROPERTY_SETTING_ENABLE_RECORD";
    private static final String PROPERTY_SETTING_ENABLE_NOTIFICATION = "PROPERTY_SETTING_ENABLE_NOTIFICATION";
    private static final String PROPERTY_SETTING_CONFIGURE = "PROPERTY_SETTING_CONFIGURE";
    private static final String PROPERTY_SETTING_CONFIGURE_PHONE = "PROPERTY_SETTING_CONFIGURE_PHONE";
    private static final String PROPERTY_SETTING_CONFIGURE_EMAIL = "PROPERTY_SETTING_CONFIGURE_EMAIL";

    private static final String PROPERTY_INBOX = "PROPERTY_INBOX";

    private static final String PROPERTY_NOT_NOW = "PROPERTY_NOT_NOW";
    private static final String PROPERTY_RATE_NOW = "PROPERTY_RATE_NOW";
    private static final String PROPERTY_TIMES_USE = "PROPERTY_TIMES_USE";

    private static final String PROPERTY_MUTE_MICROPHONE = "PROPERTY_MUTE_MICROPHONE";
    private static final String PROPERTY_SPEAKER_PHONE = "PROPERTY_SPEAKER_PHONE";


    private static final String PROPERTY_IDENTITY = "PROPERTY_IDENTITY";
    private static final String PROPERTY_ENDPOINT = "PROPERTY_ENDPOINT";
    private static final String PROPERTY_ADDRESS = "PROPERTY_ADDRESS";

    private static final String APP_PREF = "MorePhone";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_EMAIL, "");
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_EMAIL, email).commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_NAME, "");
    }

    public static void setUserName(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_NAME, name).commit();
    }

    public static String getGivenName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_GIVEN_NAME, "");
    }

    public static void setGivenName(Context context, String givenName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_GIVEN_NAME, givenName).commit();
    }

    public static String getNickName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_NICK_NAME, "");
    }

    public static void setNickName(Context context, String nickName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_NICK_NAME, nickName).commit();
    }

    public static String getUserPicture(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_PICTURE, "");
    }

    public static void setUserPicture(Context context, String picture) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_PICTURE, picture).commit();
    }


    public static String getFirstName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_FIRST_NAME, "");
    }

    public static void setFirstName(Context context, String firstName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_FIRST_NAME, firstName).commit();
    }

    public static String getLastName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_LAST_NAME, "");
    }

    public static void setLastName(Context context, String lastName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_LAST_NAME, lastName).commit();
    }

    public static String getPassword(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_USER_PASSWORD, "");
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_USER_PASSWORD, password).commit();
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_PHONE_NUMBER, "");
    }

    public static void setPhoneNumber(Context context, String phoneNumber) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_PHONE_NUMBER, phoneNumber).commit();
    }

    public static String getFriendlyName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_PHONE_FRIENDLY_NAME, "");
    }

    public static void setFriendlyName(Context context, String phoneNumber) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_PHONE_FRIENDLY_NAME, phoneNumber).commit();
    }

    public static String getPhoneNumberSid(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_PHONE_NUMBER_SID, "");
    }

    public static void setPhoneNumberSid(Context context, String phoneNumber) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_PHONE_NUMBER_SID, phoneNumber).commit();
    }

    public static boolean getInbox(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_INBOX, true);
    }

    public static void setInbox(Context context, boolean isInbox) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_INBOX, isInbox).commit();
    }



    /*--------------------------------FOR REVIEW APP------------------------------------*/

    public static boolean getNotNow(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_NOT_NOW, false);
    }

    public static void setPrefNotNow(Context context, Boolean notNow) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_NOT_NOW, notNow).commit();
    }

    public static boolean getRateNow(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_RATE_NOW, false);
    }

    public static void setPrefRateNow(Context context, Boolean rateNow) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_RATE_NOW, rateNow).commit();
    }

    public static long getTimesUse(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getLong(PROPERTY_TIMES_USE, 0);
    }

    public static void setTimesUse(Context context, long timesUse) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putLong(PROPERTY_TIMES_USE, timesUse).commit();
    }


    /*-------------------------------FOR PHONE CALL --------------------------------*/

    public static boolean getMuteMicrophone(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_MUTE_MICROPHONE, false);
    }

    public static void setMuteMicrophone(Context context, Boolean muteMicrophone) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_MUTE_MICROPHONE, muteMicrophone).commit();
    }

    public static boolean getSpeakerphone(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_SPEAKER_PHONE, false);
    }

    public static void setSpeakerphone(Context context, Boolean speakerPhone) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_SPEAKER_PHONE, speakerPhone).commit();
    }


    /*-----------------------------------SETTING-----------------------------------*/

    public static boolean getSettingEnableRecord(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_SETTING_ENABLE_RECORD, false);
    }

    public static void setSettingEnableRecord(Context context, Boolean enableRecord) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_SETTING_ENABLE_RECORD, enableRecord).commit();
    }

    public static boolean getSettingEnableNotification(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_SETTING_ENABLE_NOTIFICATION, false);
    }

    public static void setSettingEnableNotification(Context context, Boolean notification) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_SETTING_ENABLE_NOTIFICATION, notification).commit();
    }

    public static boolean getSettingConfigure(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_SETTING_CONFIGURE, false);
    }

    public static void setSettingConfigure(Context context, Boolean configure) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_SETTING_CONFIGURE, configure).commit();
    }

    public static String getSettingConfigurePhone(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_SETTING_CONFIGURE_PHONE, "");
    }

    public static void setSettingConfigurePhone(Context context, String phoneNumber) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_SETTING_CONFIGURE_PHONE, phoneNumber).commit();
    }

    public static String getSettingConfigureEmail(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_SETTING_CONFIGURE_EMAIL, "");
    }

    public static void setSettingConfigureEmail(Context context, String email) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_SETTING_CONFIGURE_EMAIL, email).commit();
    }


    public static boolean getRegisterPhoneNumber(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(PROPERTY_REGISTER_PHONE_NUMBER, false);
    }

    public static void setRegsiterPhoneNumber(Context context, Boolean registerPhoneNumber) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putBoolean(PROPERTY_REGISTER_PHONE_NUMBER, registerPhoneNumber).commit();
    }

/*------------------------------------BINDING----------------------------------------*/

    public static String getIdentity(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_IDENTITY, "");
    }

    public static void setIdentity(Context context, String identity) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_IDENTITY, identity).commit();
    }


    public static String getAddress(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_ADDRESS, "");
    }

    public static void setAddress(Context context, String address) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_ADDRESS, address).commit();
    }


    public static String getEndpoint(Context context, String identity) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PROPERTY_ENDPOINT + identity, null);
    }

    public static void setEndpoint(Context context, String endpoint, String identity) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PROPERTY_ENDPOINT + identity, endpoint).commit();
    }

    public static void removeIdentity(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().remove(PROPERTY_IDENTITY).commit();
    }

    public static void removeAddress(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().remove(PROPERTY_ADDRESS).commit();
    }

    public static void removeEndpoint(Context context, String identity){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().remove(PROPERTY_ENDPOINT + identity).commit();
    }
}
