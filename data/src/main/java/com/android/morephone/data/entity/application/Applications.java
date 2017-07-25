package com.android.morephone.data.entity.application;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 7/25/17.
 */

public class Applications {

    @SerializedName("first_page_uri")
    public String firstPageUri;
    @SerializedName("end")
    public int end;
    @SerializedName("previous_page_uri")
    public String previousPageUri;
    @SerializedName("uri")
    public String uri;
    @SerializedName("page_size")
    public int pageSize;
    @SerializedName("page")
    public int page;
    @SerializedName("applications")
    public List<Application> applications;
    @SerializedName("next_page_uri")
    public String nextPageUri;
    @SerializedName("start")
    public int start;

}
