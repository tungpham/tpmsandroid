
package com.ethan.morephone.utils;

import android.content.Context;
import android.text.BidiFormatter;
import android.util.Log;

/**
 * Methods for handling various contact data labels.
 */
public class ContactDisplayUtils {

    private static final String TAG = "ContactDisplayUtils";
    private static BidiFormatter sBidiFormatter = BidiFormatter.getInstance();


    public static boolean getVTCapability(String number) {
        Log.d(TAG, "getVTCapability   number " + number);
//        if (null != mService) {
//            try {
//                boolean vt = false;
//                vt = mService.hasVTCapability(number);
//                Log.d(TAG,
//                    "getVTCapability success number " + number + " " + vt);
//                return vt;
//            } catch (Exception e) {
//                Log.d(TAG, "getVTCapability ERROR " + e);
//            }
//        }
        return false;
    }

    private static boolean callFromQuickContactActivity(Context context) {
        String contextString = context.toString();
        String Caller = contextString.substring(
                contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        return Caller.equals("QuickContactActivity");
    }


}
