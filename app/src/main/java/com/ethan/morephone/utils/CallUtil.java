
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
import android.text.TextUtils;

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
     * A variant of {@link #getCallIntent} for starting a video call.
     */
    public static Intent getVideoCallIntent(String number, String callOrigin) {
        final Intent intent = new Intent(Intent.ACTION_CALL, getCallUri(number));
        intent.putExtra(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE,
                VideoProfile.STATE_BIDIRECTIONAL);
        if (!TextUtils.isEmpty(callOrigin)) {
//            intent.putExtra(PhoneConstants.EXTRA_CALL_ORIGIN, callOrigin);
        }
        return intent;
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

    /**
     * @return Uri that directly dials a user's voicemail inbox.
     */
    public static Uri getVoicemailUri() {
        return Uri.fromParts(PhoneAccount.SCHEME_VOICEMAIL, "", null);
    }

    /**
     * Determines if video calling is available, and if so whether presence checking is available
     * as well.
     *
     * Returns a bitmask with {@link #VIDEO_CALLING_ENABLED} to indicate that video calling is
     * available, and {@link #VIDEO_CALLING_PRESENCE} if presence indication is also available.
     *
     * @param context The context
     * @return A bit-mask describing the current video capabilities.
     */
    public static int getVideoCallingAvailability(Context context) {
        if (!PermissionsUtil.hasPermission(context, android.Manifest.permission.READ_PHONE_STATE)
                || !CompatUtils.isVideoCompatible()) {
            return VIDEO_CALLING_DISABLED;
        }
        TelecomManager telecommMgr = (TelecomManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        if (telecommMgr == null) {
            return VIDEO_CALLING_DISABLED;
        }

        List<PhoneAccountHandle> accountHandles = telecommMgr.getCallCapablePhoneAccounts();
        for (PhoneAccountHandle accountHandle : accountHandles) {
            PhoneAccount account = telecommMgr.getPhoneAccount(accountHandle);
            if (account != null) {
                if (account.hasCapabilities(PhoneAccount.CAPABILITY_VIDEO_CALLING) ||
                        account.hasCapabilities(CAPABILITY_EMERGENCY_VIDEO_CALLING)) {
                    // Builds prior to N do not have presence support.
                    if (!CompatUtils.isVideoPresenceCompatible()) {
                        return VIDEO_CALLING_ENABLED;
                    }

                    int videoCapabilities = VIDEO_CALLING_ENABLED;
                    if (account.hasCapabilities(
                            PhoneAccountSdkCompat.CAPABILITY_VIDEO_CALLING_RELIES_ON_PRESENCE)) {
                        videoCapabilities |= VIDEO_CALLING_PRESENCE;
                    }
                    return videoCapabilities;
                }
            }
        }
        return VIDEO_CALLING_DISABLED;
    }

    /**
     * Determines if one of the call capable phone accounts defined supports video calling.
     *
     * @param context The context.
     * @return {@code true} if one of the call capable phone accounts supports video calling,
     *      {@code false} otherwise.
     */
    public static boolean isVideoEnabled(Context context) {
        boolean hasVideoCap =  ((getVideoCallingAvailability(context) &
                VIDEO_CALLING_ENABLED) != 0);
        saveVideoCallConfig(context,hasVideoCap);
        return  hasVideoCap;
    }

    /**
     * save the value of video call config.
     *
     * @param context The context, enable.
     */
    public static boolean saveVideoCallConfig(Context context, boolean enable) {
        if(Settings.System.canWrite(context)) {
            return Settings.System.putInt(context.getContentResolver(),
                    CONFIG_VIDEO_CALLING,enable?ENABLE_VIDEO_CALLING:DISABLE_VIDEO_CALLING);
        }
        return false;
    }

    /**
     * Determines if one of the call capable phone accounts defined supports calling with a subject
     * specified.
     *
     * @param context The context.
     * @return {@code true} if one of the call capable phone accounts supports calling with a
     *      subject specified, {@code false} otherwise.
     */
    public static boolean isCallWithSubjectSupported(Context context) {
        if (!PermissionsUtil.hasPermission(context, android.Manifest.permission.READ_PHONE_STATE)
                || !CompatUtils.isCallSubjectCompatible()) {
            return false;
        }
        TelecomManager telecommMgr = (TelecomManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        if (telecommMgr == null) {
            return false;
        }

        List<PhoneAccountHandle> accountHandles = telecommMgr.getCallCapablePhoneAccounts();
        for (PhoneAccountHandle accountHandle : accountHandles) {
            PhoneAccount account = telecommMgr.getPhoneAccount(accountHandle);
            if (account != null && account.hasCapabilities(PhoneAccount.CAPABILITY_CALL_SUBJECT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the number is valid for videoCall
     *
     * @param number the number to call.
     * @return true if the number is valid
     *
     * @hide
     */
    public static boolean isVideoCallNumValid(String number){
        if (null == number) {
            return false;
        }
        if (number.contains("#") || (number.contains("+") && (number.indexOf("+") != 0)) ||
                number.contains(",") || number.contains(";") || number.contains("*")) {
            return false;
        }
        String norNumber = PhoneNumberHelper.normalizeNumber(number);
        if (norNumber.length() < MAX_PHONE_NUM) {
            return false;
        }
        return true;
    }



}
