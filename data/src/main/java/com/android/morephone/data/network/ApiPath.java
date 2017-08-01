package com.android.morephone.data.network;


import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.application.Applications;
import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.entity.usage.Usage;

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

    @DELETE("Accounts/{accountsid}/IncomingPhoneNumbers/{incomingPhoneNumberSid}.json")
    Call<Void> deletePhoneNumber(@Path("accountsid") String accountsid, @Path("incomingPhoneNumberSid") String incomingPhoneNumberSid);

    @FormUrlEncoded
    @POST("Accounts/{accountsid}/IncomingPhoneNumbers/{incomingPhoneNumberSid}.json")
    Call<IncomingPhoneNumber> changeFriendlyName(@Path("accountsid") String accountsid,
                                                 @Path("incomingPhoneNumberSid") String incomingPhoneNumberSid,
                                                 @Field("FriendlyName") String friendlyName);

    @FormUrlEncoded
    @POST("Accounts/{accountsid}/IncomingPhoneNumbers.json")
    Call<IncomingPhoneNumber> buyIncomingPhoneNumber(@Path("accountsid") String accountSid,
                                                     @Field("PhoneNumber") String phoneNumber);

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

    @GET("Accounts/{accountsid}/Recordings.json")
    Call<RecordListResourceResponse> getRecords(@Path("accountsid") String accountSid);

    @DELETE("Accounts/{accountsid}/Recordings/{recordingsid}.json")
    Call<Void> deleteRecord(@Path("accountsid") String accountSid, @Path("recordingsid") String recordingSid);

    /*-----------------------------------------CALL-----------------------------------------*/
    @FormUrlEncoded
    @POST("Accounts/{accountsid}/Calls.json")
    Call<com.android.morephone.data.entity.call.Call> createVoice(@Path("accountsid") String accountsid,
                                                                  @Field("From") String phoneNumberOutgoing,
                                                                  @Field("To") String phoneNumberIncoming,
                                                                  @Field("ApplicationSid") String applicationSid,
                                                                  @Field("SipAuthUsername") String sipAuthUsername,
                                                                  @Field("SipAuthPassword") String sipAuthPassword);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<Calls> getAllCalls(@Path("accountsid") String accountsid);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<Calls> getCalls(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming, @Query("From") String phoneNumberOutgoing);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<Calls> getCallsIncoming(@Path("accountsid") String accountsid, @Query("To") String phoneNumberIncoming, @Query("PageSize") int pageSize, @Query("Page") int page);

    @GET("Accounts/{accountsid}/Calls.json")
    Call<Calls> getCallsOutgoing(@Path("accountsid") String accountsid, @Query("From") String phoneNumberOutgoing, @Query("PageSize") int pageSize, @Query("Page") int page);

    @GET("Accounts/{accountsid}/Calls/{callsid}.json")
    Call<com.android.morephone.data.entity.call.Call> getCall(@Path("accountsid") String accountsid, @Path("callsid") String callsid);

    @DELETE("Accounts/{accountsid}/Calls/{callsid}.json")
    Call<Void> deleteCall(@Path("accountsid") String accountsid, @Path("callsid") String callsid);

    @GET("Accounts/{accountsid}/Calls{callsid}/Recordings/{recordingsid}.json")
    Call<RecordItem> getRecoding(@Path("accountsid") String accountsid,
                                 @Path("callsid") String callsid,
                                 @Path("recordingsid") String recordingsid);

    @DELETE("Accounts/{accountsid}/Calls{callsid}/Recordings/{recordingsid}.json")
    Call<Void> deleteCallRecoding(@Path("accountsid") String accountsid,
                                  @Path("callsid") String callsid,
                                  @Path("recordingsid") String recordingsid);


    /*-----------------------------------------USAGE-----------------------------------------*/

    @GET("Accounts/{accountsid}/Usage/Records/AllTime.json")
    Call<Usage> getUsageAllTime(@Path("accountsid") String accountsid);

    @GET("Accounts/{accountsid}/Usage/Records/AllTime.json")
    Call<Usage> getUsageAllTime(@Path("accountsid") String accountsid, @Query("PageSize") int pageSize, @Query("Page") int page, @Query("PageToken") String pageToken);

    @GET("Accounts/{accountsid}/Usage/Records/AllTime.json")
    Call<Usage> getUsageAllTime(@Path("accountsid") String accountsid, @Query("Category") String category, @Query("PageSize") int pageSize, @Query("Page") int page, @Query("PageToken") String pageToken);


    /*-----------------------------------------APPLICATION-----------------------------------------*/

    @GET("Accounts/{accountsid}/Applications.json")
    Call<Applications> getApplications(@Path("accountsid") String accountsid);

    @DELETE("Accounts/{accountsid}/Applications/{applicationSid}.json")
    Call<Void> deleteApplications(@Path("accountsid") String accountsid, String applicationSid);


}
