package com.android.morephone.data.entity.call;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Ethan on 3/7/17.
 */

public class Call implements Comparable<Call>, Parcelable {

    @SerializedName("sid")
    public String sid;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("date_updated")
    public String dateUpdated;

    @SerializedName("parent_call_sid")
    public String parentCallSid;

    @SerializedName("account_sid")
    public String accountSid;

    @SerializedName("to")
    public String to;

    @SerializedName("from")
    public String from;

    @SerializedName("to_formatted")
    public String toFormatted;

    @SerializedName("from_formatted")
    public String fromFormatted;

    @SerializedName("phone_number_sid")
    public String phoneNumberSid;

    @SerializedName("status")
    public String status;

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("end_time")
    public String endTime;

    @SerializedName("duration")
    public String duration;

    @SerializedName("price")
    public String price;

    @SerializedName("price_unit")
    public String price_unit;

    @SerializedName("direction")
    public String direction;

    @SerializedName("answered_by")
    public String answeredBy;

    @SerializedName("api_version")
    public String apiVersion;

    @SerializedName("annotation")
    public String annotation;

    @SerializedName("forwarded_from")
    public String forwardedFrom;

    @SerializedName("group_sid")
    public String groupSid;

    @SerializedName("caller_name")
    public String callerName;

    @SerializedName("uri")
    public String uri;

    @SerializedName("subresource_uris")
    public SubresourceUris subresourceUris;

    public Call(String sid, String dateCreated, String dateUpdated, String parentCallSid, String accountSid, String to, String from, String toFormatted, String fromFormatted, String phoneNumberSid, String status, String startTime, String endTime, String duration, String price, String price_unit, String direction, String answeredBy, String apiVersion, String annotation, String forwardedFrom, String groupSid, String callerName, String uri, SubresourceUris subresourceUris) {
        this.sid = sid;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.parentCallSid = parentCallSid;
        this.accountSid = accountSid;
        this.to = to;
        this.from = from;
        this.toFormatted = toFormatted;
        this.fromFormatted = fromFormatted;
        this.phoneNumberSid = phoneNumberSid;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.price = price;
        this.price_unit = price_unit;
        this.direction = direction;
        this.answeredBy = answeredBy;
        this.apiVersion = apiVersion;
        this.annotation = annotation;
        this.forwardedFrom = forwardedFrom;
        this.groupSid = groupSid;
        this.callerName = callerName;
        this.uri = uri;
        this.subresourceUris = subresourceUris;
    }

    protected Call(Parcel in) {
        sid = in.readString();
        dateCreated = in.readString();
        dateUpdated = in.readString();
        parentCallSid = in.readString();
        accountSid = in.readString();
        to = in.readString();
        from = in.readString();
        toFormatted = in.readString();
        fromFormatted = in.readString();
        phoneNumberSid = in.readString();
        status = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        duration = in.readString();
        price = in.readString();
        price_unit = in.readString();
        direction = in.readString();
        answeredBy = in.readString();
        apiVersion = in.readString();
        annotation = in.readString();
        forwardedFrom = in.readString();
        groupSid = in.readString();
        callerName = in.readString();
        uri = in.readString();
    }

    public static final Creator<Call> CREATOR = new Creator<Call>() {
        @Override
        public Call createFromParcel(Parcel in) {
            return new Call(in);
        }

        @Override
        public Call[] newArray(int size) {
            return new Call[size];
        }
    };

    @Override
    public int compareTo(Call voiceItem) {
        Date current = DateUtils.getDate(this.dateCreated);
        Date now = DateUtils.getDate(voiceItem.dateCreated);
        if (current != null && now != null) {
            if (current.after(now)) {
                return -1;
            } else {
                return 1;
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
        parcel.writeString(dateCreated);
        parcel.writeString(dateUpdated);
        parcel.writeString(parentCallSid);
        parcel.writeString(accountSid);
        parcel.writeString(to);
        parcel.writeString(from);
        parcel.writeString(toFormatted);
        parcel.writeString(fromFormatted);
        parcel.writeString(phoneNumberSid);
        parcel.writeString(status);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(duration);
        parcel.writeString(price);
        parcel.writeString(price_unit);
        parcel.writeString(direction);
        parcel.writeString(answeredBy);
        parcel.writeString(apiVersion);
        parcel.writeString(annotation);
        parcel.writeString(forwardedFrom);
        parcel.writeString(groupSid);
        parcel.writeString(callerName);
        parcel.writeString(uri);
    }

    public class SubresourceUris {
        @SerializedName("notifications")
        public String notifications;

        @SerializedName("recordings")
        public String recordings;

        public SubresourceUris(String notifications, String recordings) {
            this.notifications = notifications;
            this.recordings = recordings;
        }
    }
}
