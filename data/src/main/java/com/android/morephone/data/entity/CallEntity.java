package com.android.morephone.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallEntity {

    @SerializedName("sid")
    public String sid;

    @SerializedName("to")
    public String phoneNumberIncoming;

    @SerializedName("from")
    public String phoneNumberOutgoing;

    @SerializedName("duration")
    public long duration;

    @SerializedName("date_created")
    public String dateCreated;

    public CallEntity(String sid, String phoneNumberIncoming, String phoneNumberOutgoing, long duration, String dateCreated) {
        this.sid = sid;
        this.phoneNumberIncoming = phoneNumberIncoming;
        this.phoneNumberOutgoing = phoneNumberOutgoing;
        this.duration = duration;
        this.dateCreated = dateCreated;
    }
}
