package com.android.morephone.data.entity.phonenumbers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/30/17.
 */

public class CountryCode {

    @SerializedName("country_code")
    public String countryCode;
    public String country;
    public String uri;
    public boolean beta;
    @SerializedName("subresource_uris")
    public SubresourceUris subresourceUris;

    public class SubresourceUris{
        public String local;
    }
}
