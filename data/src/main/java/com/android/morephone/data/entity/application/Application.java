package com.android.morephone.data.entity.application;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 7/25/17.
 */

public class Application {

    @SerializedName("sid")
    public String sid;
    @SerializedName("date_created")
    public String dateCreated;
    @SerializedName("date_updated")
    public String dateUpdated;
    @SerializedName("account_sid")
    public String accountSid;
    @SerializedName("friendly_name")
    public String friendlyName;
    @SerializedName("api_version")
    public String apiVersion;
    @SerializedName("voice_url")
    public String voiceUrl;
    @SerializedName("voice_method")
    public String voiceMethod;
    @SerializedName("voice_fallback_url")
    public String voiceFallbackUrl;
    @SerializedName("voice_fallback_method")
    public String voiceFallbackMethod;
    @SerializedName("status_callback")
    public String statusCallback;
    @SerializedName("status_callback_method")
    public String statusCallbackMethod;
    @SerializedName("voice_caller_id_lookup")
    public boolean voiceCallerIdLookup;
    @SerializedName("sms_url")
    public String smsUrl;
    @SerializedName("sms_method")
    public String smsMethod;
    @SerializedName("sms_fallback_url")
    public String smsFallbackUrl;
    @SerializedName("sms_fallback_method")
    public String smsFallbackMethod;
    @SerializedName("sms_status_callback")
    public String smsStatusCallback;
    @SerializedName("message_status_callback")
    public String messageStatusCallback;
    @SerializedName("uri")
    public String uri;
}
