package com.android.morephone.data.entity.usage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 4/26/17.
 */

public class Usage {

    @SerializedName("usage_message_total")
    public int usageMessageTotal;

    @SerializedName("usage_message_spent")
    public String usageMessageSpent;

    @SerializedName("usage_message_incoming")
    public int usageMessageIncoming;

    @SerializedName("usage_message_outgoing")
    public int usageMessageOutgoing;

    @SerializedName("usage_voice_total")
    public int usageVoiceTotal;

    @SerializedName("usage_voice_spent")
    public String usageVoiceSpent;

    @SerializedName("usage_voice_incoming")
    public int usageVoiceIncoming;

    @SerializedName("usage_voice_outgoing")
    public int usageVoiceOutgoing;

    @SerializedName("usage_voice_missing")
    public int usageVoiceMissing;

}
