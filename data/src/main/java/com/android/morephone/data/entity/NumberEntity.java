package com.android.morephone.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/16/17.
 */

public class NumberEntity {

    @SerializedName("sid")
    public String sid;

    @SerializedName("phone_number")
    public String phoneNumber;

    public NumberEntity(String sid, String phoneNumber) {
        this.sid = sid;
        this.phoneNumber = phoneNumber;
    }
}
