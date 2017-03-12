package com.android.morephone.data.entity.twilio.voice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/7/17.
 */

public class VoiceItem {

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

    public VoiceItem(String sid, String dateCreated, String dateUpdated, String parentCallSid, String accountSid, String to, String from, String toFormatted, String fromFormatted, String phoneNumberSid, String status, String startTime, String endTime, String duration, String price, String price_unit, String direction, String answeredBy, String apiVersion, String annotation, String forwardedFrom, String groupSid, String callerName, String uri, SubresourceUris subresourceUris) {
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

    public class SubresourceUris{
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
