
package com.ethan.morephone.utils;

import android.widget.EdgeEffect;

/**
 * Compatibility class for {@link EdgeEffect}
 * The android.support.v4.widget.EdgeEffectCompat doesn't support customized color, so we write
 * our own and keep using EdgeEffect to customize color.
 */
public class EdgeEffectCompat {
    /**
     * Compatibility method for {@link EdgeEffect#onPull(float, float)}, which is only available
     * on Lollipop+.
     */
    public static void onPull(EdgeEffect edgeEffect, float deltaDistance, float displacement) {
        if (CompatUtils.isLollipopCompatible()) {
            edgeEffect.onPull(deltaDistance, displacement);
        } else {
            edgeEffect.onPull(deltaDistance);
        }
    }
}
