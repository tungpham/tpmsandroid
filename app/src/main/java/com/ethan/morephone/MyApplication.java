package com.ethan.morephone;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.StormpathLogger;

/**
 * Created by Ethan on 2/23/17.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            // we only want to show the logs in debug builds, for easier debugging
            Stormpath.setLogLevel(StormpathLogger.VERBOSE);
        }

        // initialize stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl("https://demo-android.apps.stormpath.io/")
                .build();
        Stormpath.init(this, stormpathConfiguration);

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
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
