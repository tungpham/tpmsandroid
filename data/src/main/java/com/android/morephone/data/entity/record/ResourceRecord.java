package com.android.morephone.data.entity.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by truongnguyen on 9/15/17.
 */

public class ResourceRecord {

    @SerializedName("records")
    public List<Record> records;

    @SerializedName("firstPageUri")
    public String firstPageUri;

    @SerializedName("nextPageUri")
    public String nextPageUri;

    @SerializedName("previousPageUri")
    public String previousPageUri;

    @SerializedName("uri")
    public String uri;

    @SerializedName("pageSize")
    public int pageSize;


    public ResourceRecord(List<Record> records, String firstPageUri, String nextPageUri, String previousPageUri, String uri, int pageSize) {
        this.records = records;
        this.firstPageUri = firstPageUri;
        this.nextPageUri = nextPageUri;
        this.previousPageUri = previousPageUri;
        this.uri = uri;
        this.pageSize = pageSize;
    }

}
