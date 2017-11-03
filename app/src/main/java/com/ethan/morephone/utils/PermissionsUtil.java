
package com.ethan.morephone.utils;

import android.Manifest.permission;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Utility class to help with runtime permissions.
 */
public class PermissionsUtil {
    // Each permission in this list is a cherry-picked permission from a particular permission
    // group. Granting a permission group enables access to all permissions in that group so we
    // only need to check a single permission in each group.
    // Note: This assumes that the app has correctly requested for all the relevant permissions
    // in its Manifest file.
    public static final String PHONE = permission.CALL_PHONE;
    public static final String CONTACTS = permission.READ_CONTACTS;
    public static final String LOCATION = permission.ACCESS_FINE_LOCATION;

    public static boolean hasPhonePermissions(Context context) {
        return hasPermission(context, PHONE);
    }

    public static boolean hasContactsPermissions(Context context) {
        return hasPermission(context, CONTACTS);
    }

    public static boolean hasLocationPermissions(Context context) {
        return hasPermission(context, LOCATION);
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasAppOp(Context context, String appOp) {
        final AppOpsManager appOpsManager =
                (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        final int mode = appOpsManager.checkOpNoThrow(appOp, Process.myUid(),
                context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public static void registerPermissionReceiver(Context context, BroadcastReceiver receiver,
                                                  String permission) {
        final IntentFilter filter = new IntentFilter(permission);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public static void unregisterPermissionReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static void notifyPermissionGranted(Context context, String permission) {
        final Intent intent = new Intent(permission);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
