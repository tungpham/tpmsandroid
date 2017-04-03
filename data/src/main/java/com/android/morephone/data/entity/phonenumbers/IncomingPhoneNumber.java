package com.android.morephone.data.entity.phonenumbers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 4/3/17.
 */

public class IncomingPhoneNumber {

    public String sid;

    @SerializedName("account_sid")
    public String accountSid;

    @SerializedName("friendly_name")
    public String friendlyName;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("voice_url")
    public String voiceUrl;

    @SerializedName("voice_method")
    public String voiceMethod;

    @SerializedName("voice_fallback_url")
    public String voiceFallbackUrl;

    @SerializedName("voice_fallback_method")
    public String voiceFallbackMethod;

    @SerializedName("voice_caller_id_lookup")
    public boolean voiceCallerIdLookup;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("date_updated")
    public String dateUpdated;

    @SerializedName("sms_url")
    public String smsUrl;

    @SerializedName("sms_method")
    public String smsMethod;

    @SerializedName("sms_fallback_url")
    public String smsFallbackUrl;

    @SerializedName("sms_fallback_method")
    public String smsFallbackMethod;

    @SerializedName("address_requirements")
    public String addressRequirements;

    public boolean beta;

    @SerializedName("voice_receive_mode")
    public String voiceReceiveMode;

    @SerializedName("status_callback")
    public String statusCallback;

    @SerializedName("status_callback_method")
    public String statusCallbackMethod;

    @SerializedName("api_version")
    public String apiVersion;

    @SerializedName("voice_application_sid")
    public String voiceApplicationSid;

    @SerializedName("sms_application_sid")
    public String smsApplicationSid;

    @SerializedName("trunk_sid")
    public String trunkSid;

    @SerializedName("emergency_status")
    public String emergencyStatus;

    @SerializedName("emergency_address_sid")
    public String emergencyAddressSid;

    public String uri;

    public Capabilities capabilities;

    public class Capabilities {

        public boolean voice;
        public boolean sms;
        public boolean mms;
        public boolean fax;
    }
}
