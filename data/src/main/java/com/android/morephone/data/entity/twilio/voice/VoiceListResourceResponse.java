package com.android.morephone.data.entity.twilio.voice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 3/7/17.
 */

public class VoiceListResourceResponse {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    @SerializedName("end")
    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    @SerializedName("calls")
    public List<VoiceItem> calls;

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

    public VoiceListResourceResponse(String firstPageUri, int end, String previousPageUri, List<VoiceItem> calls, String uri, int pageSize, int start, String nextPageUri, int page) {
        this.firstPageUri = firstPageUri;
        this.end = end;
        this.previousPageUri = previousPageUri;
        this.calls = calls;
        this.uri = uri;
        this.pageSize = pageSize;
        this.start = start;
        this.nextPageUri = nextPageUri;
        this.page = page;
    }
}
