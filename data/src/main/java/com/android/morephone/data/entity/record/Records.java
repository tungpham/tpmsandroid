package com.android.morephone.data.entity.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 3/7/17.
 */

public class Records {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    @SerializedName("end")
    public int end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    @SerializedName("recordings")
    public List<Record> recordings;

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
