package com.android.morephone.data.log;

import android.util.Log;


public class DebugTool {
    private static final boolean DEBUG = true;

    private static final String TAG_PREFIX = "TAG---";

    public static void logD(String msg) {
        if (DEBUG) {
            if (msg == null) {
                msg = "";
            }
            Log.d(TAG_PREFIX, msg);
        }
    }

    public static void logE(String msg) {
        if (DEBUG) {
            if (msg == null) {
                msg = "";
            }
            Log.d(TAG_PREFIX, msg);
        }
    }

    public static void printStackTrace(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }
}
