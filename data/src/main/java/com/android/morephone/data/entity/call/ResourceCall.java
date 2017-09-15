package com.android.morephone.data.entity.call;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by truongnguyen on 9/15/17.
 */

public class ResourceCall {

    @SerializedName("records")
    public List<Call> records;

    @SerializedName("incomingFirstPageUri")
    public String incomingFirstPageUri;

    @SerializedName("incomingNextPageUri")
    public String incomingNextPageUri;

    @SerializedName("incomingPreviousPageUri")
    public String incomingPreviousPageUri;

    @SerializedName("incomingUri")
    public String incomingUri;

    @SerializedName("outgoingFirstPageUri")
    public String outgoingFirstPageUri;

    @SerializedName("outgoingNextPageUri")
    public String outgoingNextPageUri;

    @SerializedName("outgoingPreviousPageUri")
    public String outgoingPreviousPageUri;

    @SerializedName("outgoingUri")
    public String outgoingUri;

    @SerializedName("pageSize")
    public int pageSize;


    public ResourceCall(List<Call> records, String incomingFirstPageUri, String incomingNextPageUri, String incomingPreviousPageUri, String incomingUri, String outgoingFirstPageUri, String outgoingNextPageUri, String outgoingPreviousPageUri, String outgoingUri, int pageSize) {
        this.records = records;
        this.incomingFirstPageUri = incomingFirstPageUri;
        this.incomingNextPageUri = incomingNextPageUri;
        this.incomingPreviousPageUri = incomingPreviousPageUri;
        this.incomingUri = incomingUri;
        this.outgoingFirstPageUri = outgoingFirstPageUri;
        this.outgoingNextPageUri = outgoingNextPageUri;
        this.outgoingPreviousPageUri = outgoingPreviousPageUri;
        this.outgoingUri = outgoingUri;
        this.pageSize = pageSize;
    }
}
