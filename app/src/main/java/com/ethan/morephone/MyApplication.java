package com.ethan.morephone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.ethan.morephone.presentation.buy.payment.checkout.Billing;
import com.ethan.morephone.presentation.buy.payment.checkout.PlayStoreListener;
import com.facebook.stetho.Stetho;

import javax.annotation.Nonnull;


/**
 * Created by Ethan on 2/23/17.
 */

public class MyApplication extends MultiDexApplication {

    @Nonnull
    private final Billing mBilling = new Billing(this, new Billing.DefaultConfiguration() {
        @Nonnull
        @Override
        public String getPublicKey() {
            // encrypted public key of the app. Plain version can be found in Google Play's Developer
            // Console in Service & APIs section under "YOUR LICENSE KEY FOR THIS APPLICATION" title.
            // A naive encryption algorithm is used to "protect" the key. See more about key protection
            // here: https://developer.android.com/google/play/billing/billing_best_practices.html#key
            return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhziPq0YT4evePmkFel/p8/paiABLGTbCCRqc9rSU1OypyOqpSD851o6VeeC7hDJyyYPbQnGtB/7sHYP+brFNr2Nqrlmb90aVh0FO/40DQO0/aMuhgyECpLJ7IJXZ9MLuqOscZ2qnatVBbpTHy7sJb/a8ZCXOXlMHpPlcYI7kaCJ+0nm2t16ltDF0CxMz5FUoUyb6KfqFkHMe/48HcGfQVoxP3JOpDe5tP3KITAXDsOEXCLWpYVNFtvM0wImOYmAc6BqRHvNL2WTIex87DWtnQfxt3qrRfn0jv/rDv8t5hguutsToHC9pr/DWEY4wPkkGFdU308tMe+l9HkWpxK/jqQIDAQAB";
        }
    });


//    private static final String USER_PREFS = "com.google.android.gms.samples.wallet.USER_PREFS";
//    private static final String KEY_USERNAME = "com.google.android.gms.samples.wallet.KEY_USERNAME";
//    private String mUserName;

    // Not being saved in shared preferences to let users try new addresses
    // between app invocations
    private boolean mAddressValidForPromo;

    private SharedPreferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (BuildConfig.DEBUG) {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build());
//        }

//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());

//        mPrefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
//        mUserName = mPrefs.getString(KEY_USERNAME, null);

        mBilling.addPlayStoreListener(new PlayStoreListener() {
            @Override
            public void onPurchasesChanged() {
                Toast.makeText(MyApplication.this, R.string.purchases_changed, Toast.LENGTH_LONG).show();
            }
        });

        Stetho.initializeWithDefaults(this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
//




    /**
     * Returns an instance of {@link MyApplication} attached to the passed activity.
     */
    public static MyApplication get(Activity activity) {
        return (MyApplication) activity.getApplication();
    }

    @Nonnull
    public Billing getBilling() {
        return mBilling;
    }

//    public boolean isLoggedIn() {
//        return mUserName != null;
//    }
//
//    public void login(String userName) {
//        mUserName = userName;
//        mPrefs.edit().putString(KEY_USERNAME, mUserName).commit();
//    }
//
//    public void logout() {
//        mUserName = null;
//        mPrefs.edit().remove(KEY_USERNAME).commit();
//    }
//
//    public String getAccountName() {
//        return mPrefs.getString(KEY_USERNAME, null);
//    }
//
//    public boolean isAddressValidForPromo() {
//        return mAddressValidForPromo;
//    }
//
//    public void setAddressValidForPromo(boolean addressValidForPromo) {
//        this.mAddressValidForPromo = addressValidForPromo;
//    }
}
