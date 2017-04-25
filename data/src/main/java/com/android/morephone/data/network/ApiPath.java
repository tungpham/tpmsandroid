package com.android.morephone.data.network;


import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.entity.twilio.voice.VoiceListResourceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by AnPEthan on 8/11/2016.
 */
interface ApiPath {


    /*-----------------------------------------FAKE DATA-----------------------------------------*/
    @GET("master/Design/fake_data.json")
    Call<FakeData> getFakeDAta();

    @GET("master/countrycode.json")
    Call<List<AvailableCountry>> getCountryCode();

    /*--------------------------------Available Phone Numbers----------------------------------*/

    @GET("Accounts/{accountsid}/AvailablePhoneNumbers.json")
    Call<AvailableCountries> getAvailableCountries(@Path("accountsid") String accountsid);

    @GET("Accounts/{accountsid}/AvailablePhoneNumbers/{countrycode}/Local.json")
    Call<AvailablePhoneNumbers> getAvailablePhoneNumbers(@Path("accountsid") String accountsid,
                                                         @Path("countrycode") String countryCode,
                                                         @Query("Contains") String contains,
                                                         @Query("SmsEnabled") boolean smsEnabled,
                                                         @Query("MmsEnabled") boolean mmsEnable,
                                                         @Query("VoiceEnabled") boolean voiceEnabled);

    /*--------------------------------Incoming Phone Numbers ----------------------------------*/

    @GET("Accounts/{accountsid}/IncomingPhoneNumbers.json")
    Call<IncomingPhoneNumbers> getIncomingPhoneNumbers(@Path("accountsid") String accountsid);

    @DELETE("Accounts/{accountsid}/IncomingPhoneNumbers/{incomingPhoneNumberSid}")
    Call<Void> deletePhoneNumber(@Path("accountsid") String accountsid, @Path("incomingPhoneNumberSid") String incomingPhoneNumberSid);

    @FormUrlEncoded
    @POST("Accounts/{accountsid}/IncomingPhoneNumbers/{incomingPhoneNumberSid}.json")
    Call<IncomingPhoneNumber> changeFriendlyName(@Path("accountsid") String accountsid,
                                                 @Path("incomingPhoneNumberSid") String incomingPhoneNumberSid,
                                                 @Field("FriendlyName") String friendlyName);

    /*-----------------------------------------MESSAGE-----------------------------------------*/

    @FormUrlEncoded
    @POST("Accounts/{accountsid}/Messages.json")
    Call<MessageItem> createMessage(@Path("accountsid") String accountsid,
                                    @Field("From") String from,
                                    @Field("To") String to,
                                    @Field("Body") String body);

    @GET("Accounts/{accountsid}/Messages.json")
    Call<MessageListResourceResponse> getAllMessageListResource(@Path("accountsid") String accountsid);

    @GET("Accounts/{accountsid}/Messages.json")
    Call<MessageListResourceResponse> getMessages(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming, @Query("From") String phoneNumberOutgoing);

    @GET("Accounts/{accountsid}/Messages.json")
    Call<MessageListResourceResponse> getMessagesIncoming(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming);

    @GET("Accounts/{accountsid}/Messages.json")
    Call<MessageListResourceResponse> getMessagesOutgoing(@Path("accountsid") String accountsid, @Query("From") String phoneNumberOutgoing);

    @DELETE("Accounts/{accountsid}/Messages/{messagesid}.json")
    Call<Void> deleteMessage(@Path("accountsid") String accountsid, @Path("messagesid") String messagesid);

    /*-----------------------------------------RECORDINGS-----------------------------------------*/

    @GET("Accounts/{accountsid}/Calls/{callsid}/Recordings/{recordingsid}.json")
    Call<RecordItem> getRecordItem(@Path("accountsid") String accountSid, @Path("callsid") String callSid, @Path("recordingsid") String recordingSid);

    @GET("Accounts/{accountsid}/Calls/{callsid}/Recordings.json")
    Call<RecordListResourceResponse> getRecordListResource(@Path("accountsid") String accountSid, @Path("callsid") String callSid);

    @DELETE("Accounts/{accountsid}/Recordings/{recordingsid}.json")
    Call<Void> deleteRecord(@Path("accountsid") String accountSid, @Path("recordingsid") String recordingSid);

    /*-----------------------------------------VOICE-----------------------------------------*/
    @FormUrlEncoded
    @POST("Accounts/{accountsid}/Calls.json")
    Call<VoiceItem> createVoice(@Path("accountsid") String accountsid,
                                @Field("From") String phoneNumberOutgoing,
                                @Field("To") String phoneNumberIncoming,
                                @Field("ApplicationSid") String applicationSid,
                                @Field("SipAuthUsername") String sipAuthUsername,
                                @Field("SipAuthPassword") String sipAuthPassword);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<VoiceListResourceResponse> getAllVoiceListResource(@Path("accountsid") String accountsid);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<VoiceListResourceResponse> getVoices(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming, @Query("From") String phoneNumberOutgoing);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<VoiceListResourceResponse> getVoiceByIncoming(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<VoiceListResourceResponse> getVoicesOutgoing(@Path("accountsid") String accountsid, @Query("From") String phoneNumberOutgoing);

    @DELETE("Accounts/{accountsid}/Calls/{callsid}.json")
    Call<Void> deleteVoice(@Path("accountsid") String accountsid, @Path("callsid") String callsid);


    @GET("Accounts/{accountsid}/Calls{callsid}/Recordings/{recordingsid}.json")
    Call<RecordItem> getRecoding(@Path("accountsid") String accountsid,
                                 @Path("callsid") String callsid,
                                 @Path("recordingsid") String recordingsid);

    @DELETE("Accounts/{accountsid}/Calls{callsid}/Recordings/{recordingsid}.json")
    Call<Void> deleteCallRecoding(@Path("accountsid") String accountsid,
                                  @Path("callsid") String callsid,
                                  @Path("recordingsid") String recordingsid);


}
