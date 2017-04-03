package com.android.morephone.data.entity.phonenumbers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 4/3/17.
 */

public class IncomingPhoneNumbers {

    @SerializedName("first_page_uri")
    public String firstPageUri;

    public String end;

    @SerializedName("previous_page_uri")
    public String previousPageUri;

    public String uri;

    @SerializedName("page_size")
    public int pageSize;

    public int start;

    @SerializedName("next_page_uri")
    public String nextPageUri;

    public int page;

    @SerializedName("incoming_phone_numbers")
    public List<IncomingPhoneNumber> incomingPhoneNumbers;

}
