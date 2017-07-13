package com.android.morephone.data.entity.record;

import android.support.annotation.NonNull;

import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Ethan on 4/5/17.
 */

public class Record implements Comparable<Record> {

    public String sid;

    @SerializedName("account_sid")
    public String accountSid;

    @SerializedName("call_sid")
    public String callSid;

    @SerializedName("phone_number")
    public String phoneNumber;

    public String duration;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("api_version")
    public String apiVersion;

    @SerializedName("date_updated")
    public String dateUpdated;

    public String status;

    public String source;

    public int channels;

    public String price;

    @SerializedName("price_unit")
    public String priceUnit;

    public String uri;

    public boolean isComing;

    public Record(String sid, String accountSid, String callSid, String phoneNumber, String duration, String dateCreated, String apiVersion, String dateUpdated, String status, String source, int channels, String price, String priceUnit, String uri, boolean isComing) {
        this.sid = sid;
        this.accountSid = accountSid;
        this.callSid = callSid;
        this.phoneNumber = phoneNumber;
        this.duration = duration;
        this.dateCreated = dateCreated;
        this.apiVersion = apiVersion;
        this.dateUpdated = dateUpdated;
        this.status = status;
        this.source = source;
        this.channels = channels;
        this.price = price;
        this.priceUnit = priceUnit;
        this.uri = uri;
        this.isComing = isComing;
    }

    @Override
    public int compareTo(@NonNull Record record) {
        Date current = DateUtils.getDate(this.dateCreated);
        Date now = DateUtils.getDate(record.dateCreated);
        if (current != null && now != null) {
            if (current.after(now)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }
}
