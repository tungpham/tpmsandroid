package com.ethan.morephone.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Ethan on 7/28/17.
 */

public class LifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private final String TAG = "LifecycleHandler";

    private static int sResumed;
    private static int sPaused;
    private static int sStarted;
    private static int sStopped;
    private static int sActivityCounter;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        sActivityCounter++;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        sActivityCounter--;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        sResumed++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        sPaused++;
        Log.i("test", "application is in foreground: " + (sResumed > sPaused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        sStarted++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        sStopped++;
        Log.i("test", "application is visible: " + (sStarted > sStopped));
    }

    public static boolean isApplicationVisible() {
        return sStarted > sStopped;
    }

    public static boolean isApplicationInForeground() {
        return sResumed > sPaused;
    }

    public static boolean isNoActivitiesAlive() {
        return sActivityCounter <= 0;
    }
}
