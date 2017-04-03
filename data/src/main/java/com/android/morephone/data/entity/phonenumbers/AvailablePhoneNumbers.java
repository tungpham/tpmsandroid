package com.android.morephone.data.entity.phonenumbers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethan on 4/2/17.
 */

public class AvailablePhoneNumbers {

    public String uri;

    @SerializedName("available_phone_numbers")
    public List<AvailablePhoneNumber> availableAvailablePhoneNumbers;
}
