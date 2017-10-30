package com.android.morephone.data.entity.record;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Ethan on 4/5/17.
 */

public class Record implements Comparable<Record>, Parcelable {

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

    protected Record(Parcel in) {
        sid = in.readString();
        accountSid = in.readString();
        callSid = in.readString();
        phoneNumber = in.readString();
        duration = in.readString();
        dateCreated = in.readString();
        apiVersion = in.readString();
        dateUpdated = in.readString();
        status = in.readString();
        source = in.readString();
        channels = in.readInt();
        price = in.readString();
        priceUnit = in.readString();
        uri = in.readString();
        isComing = in.readByte() != 0;
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sid);
        parcel.writeString(accountSid);
        parcel.writeString(callSid);
        parcel.writeString(phoneNumber);
        parcel.writeString(duration);
        parcel.writeString(dateCreated);
        parcel.writeString(apiVersion);
        parcel.writeString(dateUpdated);
        parcel.writeString(status);
        parcel.writeString(source);
        parcel.writeInt(channels);
        parcel.writeString(price);
        parcel.writeString(priceUnit);
        parcel.writeString(uri);
        parcel.writeByte((byte) (isComing ? 1 : 0));
    }
}
