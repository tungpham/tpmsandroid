package com.android.morephone.data.entity;

import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static com.android.morephone.data.utils.DateUtils.getDate;

/**
 * Created by Ethan on 2/21/17.
 */

public class MessageItem implements Comparable<MessageItem> {

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

    public class SubresourceUris {

        @SerializedName("media")
        private String media;

        public SubresourceUris(String media) {
            this.media = media;
        }

    }

}
