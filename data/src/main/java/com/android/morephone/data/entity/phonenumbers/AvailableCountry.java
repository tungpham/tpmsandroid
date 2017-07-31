package com.android.morephone.data.entity.phonenumbers;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 3/30/17.
 */

public class AvailableCountry implements Comparable<AvailableCountry> {

    @SerializedName("country_code")
    public String countryCode;
    public String country;
    public String uri;
    public boolean beta;
    @SerializedName("subresource_uris")
    public SubresourceUris subresourceUris;

    @Override
    public int compareTo(@NonNull AvailableCountry availableCountry) {
        return country.compareTo(availableCountry.country);
    }

    public class SubresourceUris{
        public String local;
    }
}
