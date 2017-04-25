package com.android.morephone.data.entity.call;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 3/7/17.
 */

public class Calls {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    @SerializedName("end")
    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    @SerializedName("calls")
    public List<Call> calls;

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

    public Calls(String firstPageUri, int end, String previousPageUri, List<Call> calls, String uri, int pageSize, int start, String nextPageUri, int page) {
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
