package com.android.morephone.data.entity.twilio;

import com.android.morephone.data.entity.MessageItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 3/1/17.
 */

public class MessageListResourceResponse {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    @SerializedName("end")
    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    @SerializedName("messages")
    public List<MessageItem> messages;

    @SerializedName("uri")
    public String uri;

    @SerializedName("page_size")
    public int pageSize;

    @SerializedName("start")
    public int start;

    @SerializedName("next_page_uri")
    public String nextPageUri;

    @SerializedName("page")
    public int page;

    public MessageListResourceResponse(String firstPageUri,
                                       int end,
                                       String previousPageUri,
                                       List<MessageItem> messages,
                                       String uri,
                                       int pageSize,
                                       int start,
                                       String nextPageUri,
                                       int page) {
        this.firstPageUri = firstPageUri;
        this.end = end;
        this.previousPageUri = previousPageUri;
        this.messages = messages;
        this.uri = uri;
        this.pageSize = pageSize;
        this.start = start;
        this.nextPageUri = nextPageUri;
        this.page = page;
    }
}
