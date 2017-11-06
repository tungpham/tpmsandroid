package com.ethan.morephone.utils;

import android.os.Build;

public final class CompatUtils {

    /**
     * Determines if this version is compatible with Lollipop-specific APIs. Can also force the
     * version to be lower through SdkVersionOverride.
     *
     * @return {@code true} if call subject is a feature on this device, {@code false} otherwise.
     */
    public static boolean isLollipopCompatible() {
        return SdkVersionOverride.getSdkVersion(Build.VERSION_CODES.LOLLIPOP)
                >= Build.VERSION_CODES.LOLLIPOP;
    }
}
