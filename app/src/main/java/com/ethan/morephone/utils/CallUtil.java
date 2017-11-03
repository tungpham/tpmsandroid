
package com.ethan.morephone.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;

import java.util.List;

/**
 * Utilities related to calls that can be used by non system apps. These
 * use {@link Intent#ACTION_CALL} instead of ACTION_CALL_PRIVILEGED.
 *
 * The privileged version of this util exists inside Dialer.
 */
public class CallUtil {

    /**
     * Indicates that the video calling is not available.
     */
    public static final int VIDEO_CALLING_DISABLED = 0;

    /**
     * Indicates that video calling is enabled, regardless of presence status.
     */
    public static final int VIDEO_CALLING_ENABLED = 1;

    /**
     * Indicates that video calling is enabled, but the availability of video call affordances is
     * determined by the presence status associated with contacts.
     */
    public static final int VIDEO_CALLING_PRESENCE = 2;

    /*Enable Video calling irrespective of video capabilities*/
    public static final int ENABLE_VIDEO_CALLING = 1;
    /*Disable Video calling irrespective of video capabilities*/
    public static final int DISABLE_VIDEO_CALLING = 2;
    public static final String CONFIG_VIDEO_CALLING = "config_video_calling";
    public static final String DIALOG_VIDEO_CALLING = "display_video_call_dialog";
    private static AlertDialog mAlertDialog = null;
    private static final int MAX_PHONE_NUM = 7;
    /* The below definition should match with the one in PhoneAccount.java file
       present in frameworks/base git */
    private static final int CAPABILITY_EMERGENCY_VIDEO_CALLING = 0x200;

    /**
     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
     * automatically.
     */
    public static Intent getCallWithSubjectIntent(String number,
                                                  PhoneAccountHandle phoneAccountHandle, String callSubject) {

        final Intent intent = getCallIntent(getCallUri(number));
        intent.putExtra(TelecomManager.EXTRA_CALL_SUBJECT, callSubject);
        if (phoneAccountHandle != null) {
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
        }
        return intent;
    }

    /**
     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
     * automatically.
     */
    public static Intent getCallIntent(String number) {
        return getCallIntent(getCallUri(number));
    }

    /**
     * Return an Intent for making a phone call. A given Uri will be used as is (without any
     * sanity check).
     */
    public static Intent getCallIntent(Uri uri) {
        return new Intent(Intent.ACTION_CALL, uri);
    }


    /**
     * Return Uri with an appropriate scheme, accepting both SIP and usual phone call
     * numbers.
     */
    public static Uri getCallUri(String number) {
        if (PhoneNumberHelper.isUriNumber(number)) {
             return Uri.fromParts(PhoneAccount.SCHEME_SIP, number, null);
        }
        return Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null);
    }




}
