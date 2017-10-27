package com.android.morephone.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static com.android.morephone.data.utils.DateUtils.getDate;

/**
 * Created by Ethan on 2/21/17.
 */

public class MessageItem implements Comparable<MessageItem>, Parcelable {

    @SerializedName("sid")
    public String sid;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("date_updated")
    public String dateUpdated;

    @SerializedName("date_sent")
    public String dateSent;

    @SerializedName("account_sid")
    public String accountSid;

    @SerializedName("to")
    public String to;

    @SerializedName("from")
    public String from;

    @SerializedName("messaging_service_sid")
    public String messagingServiceSid;

    @SerializedName("body")
    public String body;

    @SerializedName("status")
    public String status;

    @SerializedName("num_segments")
    public String numSegments;

    @SerializedName("num_media")
    public String numMedia;

    @SerializedName("direction")
    public String direction;

    @SerializedName("api_version")
    public String apiVersion;

    @SerializedName("price")
    public String price;

    @SerializedName("price_unit")
    public String priceUnit;

    @SerializedName("error_code")
    public String errorCode;

    @SerializedName("error_message")
    public String errorMessage;

    @SerializedName("uri")
    public String uri;

    @SerializedName("subresource_uris")
    public SubresourceUris subresourceUris;

    public boolean isLoading = false;
    public boolean isSendFail = false;


    public MessageItem(String id,
                       String dateCreated,
                       String dateUpdated,
                       String dateSent,
                       String accountSid,
                       String to,
                       String from,
                       String messagingServiceSid,
                       String body,
                       String status,
                       String numSegments,
                       String numMedia,
                       String direction,
                       String apiVersion,
                       String price,
                       String priceUnit,
                       String errorCode,
                       String errorMessage,
                       String uri,
                       SubresourceUris subresourceUris
    ) {
        this.sid = id;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateSent = dateSent;
        this.accountSid = accountSid;
        this.to = to;
        this.from = from;
        this.messagingServiceSid = messagingServiceSid;
        this.body = body;
        this.status = status;
        this.numSegments = numSegments;
        this.numMedia = numMedia;
        this.direction = direction;
        this.apiVersion = apiVersion;
        this.price = price;
        this.priceUnit = priceUnit;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.uri = uri;
        this.subresourceUris = subresourceUris;
    }

    protected MessageItem(Parcel in) {
        sid = in.readString();
        dateCreated = in.readString();
        dateUpdated = in.readString();
        dateSent = in.readString();
        accountSid = in.readString();
        to = in.readString();
        from = in.readString();
        messagingServiceSid = in.readString();
        body = in.readString();
        status = in.readString();
        numSegments = in.readString();
        numMedia = in.readString();
        direction = in.readString();
        apiVersion = in.readString();
        price = in.readString();
        priceUnit = in.readString();
        errorCode = in.readString();
        errorMessage = in.readString();
        uri = in.readString();
        isLoading = in.readByte() != 0;
        isSendFail = in.readByte() != 0;
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel in) {
            return new MessageItem(in);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };

    @Override
    public int compareTo(MessageItem messageItem) {
        Date current = DateUtils.getDate(this.dateSent);
        Date now = DateUtils.getDate(messageItem.dateSent);
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
        parcel.writeString(dateCreated);
        parcel.writeString(dateUpdated);
        parcel.writeString(dateSent);
        parcel.writeString(accountSid);
        parcel.writeString(to);
        parcel.writeString(from);
        parcel.writeString(messagingServiceSid);
        parcel.writeString(body);
        parcel.writeString(status);
        parcel.writeString(numSegments);
        parcel.writeString(numMedia);
        parcel.writeString(direction);
        parcel.writeString(apiVersion);
        parcel.writeString(price);
        parcel.writeString(priceUnit);
        parcel.writeString(errorCode);
        parcel.writeString(errorMessage);
        parcel.writeString(uri);
        parcel.writeByte((byte) (isLoading ? 1 : 0));
        parcel.writeByte((byte) (isSendFail ? 1 : 0));
    }

    public class SubresourceUris {

        @SerializedName("media")
        private String media;

        public SubresourceUris(String media) {
            this.media = media;
        }

    }

}
