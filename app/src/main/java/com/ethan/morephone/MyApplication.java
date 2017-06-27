package com.ethan.morephone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


/**
 * Created by Ethan on 2/23/17.
 */

public class MyApplication extends MultiDexApplication {

    private static final String USER_PREFS = "com.google.android.gms.samples.wallet.USER_PREFS";
    private static final String KEY_USERNAME = "com.google.android.gms.samples.wallet.KEY_USERNAME";
    private String mUserName;

    // Not being saved in shared preferences to let users try new addresses
    // between app invocations
    private boolean mAddressValidForPromo;

    private SharedPreferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        mPrefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        mUserName = mPrefs.getString(KEY_USERNAME, null);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isLoggedIn() {
        return mUserName != null;
    }

    public void login(String userName) {
        mUserName = userName;
        mPrefs.edit().putString(KEY_USERNAME, mUserName).commit();
    }

    public void logout() {
        mUserName = null;
        mPrefs.edit().remove(KEY_USERNAME).commit();
    }

    public String getAccountName() {
        return mPrefs.getString(KEY_USERNAME, null);
    }

    public boolean isAddressValidForPromo() {
        return mAddressValidForPromo;
    }

    public void setAddressValidForPromo(boolean addressValidForPromo) {
        this.mAddressValidForPromo = addressValidForPromo;
    }
}
