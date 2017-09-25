
package com.ethan.morephone.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.BidiFormatter;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ethan.morephone.R;

import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

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
        if (Caller.equals("QuickContactActivity")) {
            return true;
        }
        return false;
    }


}
