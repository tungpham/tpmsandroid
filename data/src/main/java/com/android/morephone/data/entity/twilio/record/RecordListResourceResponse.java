package com.android.morephone.data.entity.twilio.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 3/7/17.
 */

public class RecordListResourceResponse {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    @SerializedName("end")
    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    @SerializedName("recordings")
    public List<RecordItem> recordings;

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

}
