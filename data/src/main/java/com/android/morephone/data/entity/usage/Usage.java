package com.android.morephone.data.entity.usage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 4/26/17.
 */

public class Usage {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    public String uri;

    public int pageSize;

    public int start;

    @SerializedName("usage_records")
    public List<UsageRecord> usageRecords;

    @SerializedName("next_page_uri")
    public  String nextPageUri;

    public int page;
}
