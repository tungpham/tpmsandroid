package com.ethan.morephone.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Aspsine on 2015/7/29.
 */
public class Utils {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M of " + DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static String getTimeDate(long time) {
        Format formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(new Date(time));
    }

    public static String formatDate(String date) {
        SimpleDateFormat in = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");
        SimpleDateFormat out = new SimpleDateFormat("dd-MM HH:mm:ss");

        try {
            Date time = in.parse(date);
            return out.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date getDate(String date) {
        SimpleDateFormat in = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");

        try {
            Date time = in.parse(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void rateApp(Context activity) {
        final String appName = activity.getPackageName();// your application package
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static String getVersion(Context context) {
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getTimePerSecond(int speed, long sofar, long total) {
        long duration = total - sofar;
        if (speed > 0) {
            int sec = (int) duration / 1024 / speed;
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(tz);
            String time = df.format(new Date(sec * 1000L));
            return time;
        } else {
            return "∞";
        }

    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "");
        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static String getHost(String str) {
        try {
            str = new URL(str).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getUid(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getKeyHash(Context context) {
        // Add code to print out the key hash
        String keyHash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", keyHash);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return keyHash;
    }

    public static String getTelephone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    public static boolean isInternetAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getPageToken(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            String[] splitAnd = uri.split("&");
            if (splitAnd != null && splitAnd.length > 0) {
                for (String str : splitAnd) {
                    if (str.startsWith("PageToken")) {
                        String[] splitEqual = str.split("=");
                        if (splitEqual != null && splitEqual.length > 0) {
                            return splitEqual[1];
                        }
                    }
                }
            }
        }
        return "";
    }


}
