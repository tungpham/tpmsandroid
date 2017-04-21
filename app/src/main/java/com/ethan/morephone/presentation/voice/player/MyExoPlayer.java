package com.ethan.morephone.presentation.voice.player;

import android.content.Context;

import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.trackselection.TrackSelector;

/**
 * Created by Ethan on 4/18/17.
 */

public class MyExoPlayer extends SimpleExoPlayer {

    protected MyExoPlayer(Context context, TrackSelector trackSelector, LoadControl loadControl, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode, long allowedVideoJoiningTimeMs) {
        super(context, trackSelector, loadControl, drmSessionManager, extensionRendererMode, allowedVideoJoiningTimeMs);
    }

}
