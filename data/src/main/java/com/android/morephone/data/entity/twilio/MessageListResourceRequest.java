package com.android.morephone.data.entity.twilio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/1/17.
 */

public class MessageListResourceRequest {

    @SerializedName("To")
    public String to;
    @SerializedName("Body")
    public String body;
    @SerializedName("From")
    public String from;

    public MessageListResourceRequest(String to, String body, String from) {
        this.to = to;
        this.body = body;
        this.from = from;
    }
}
