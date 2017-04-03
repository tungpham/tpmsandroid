package com.android.morephone.data.entity.phonenumbers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 4/2/17.
 */

public class AvailablePhoneNumber {

    @SerializedName("friendly_name")
    public String friendlyName;

    @SerializedName("phone_number")
    public String phoneNumber;

    public String lata;

    @SerializedName("rate_center")
    public String rateCenter;

    public double latitude;

    public double longitude;

    public String region;

    @SerializedName("postal_code")
    public String postalCode;

    @SerializedName("iso_country")
    public String isoCountry;

    @SerializedName("address_requirements")
    public String addressRequirements;

    public boolean beta;

    public Capabilities capabilities;

    public class Capabilities {

        public boolean voice;
        public boolean SMS;
        public boolean MMS;
        public boolean fax;
    }
}
