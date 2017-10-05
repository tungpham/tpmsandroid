package com.android.morephone.data.network;


import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.call.ResourceCall;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.conversation.ResourceMessage;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.purchase.MorePhonePurchase;
import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.entity.record.ResourceRecord;
import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.entity.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by AnPEthan on 8/11/2016.
 */
interface ApiMorePhonePath {

    /*-----------------------------------------USER-----------------------------------------*/
    @POST("user")
    Call<BaseResponse<User>> createUser(@Body User user);

    @PUT("user/{id}/token")
    Call<BaseResponse<User>> updateFcmToken(@Path("id") String id, @Query("token") String token);


    /*-----------------------------------------PHONE NUMBER-----------------------------------------*/

    @POST("phone-number")
    Call<BaseResponse<PhoneNumber>> createPhoneNumber(@Body PhoneNumber phoneNumber);

    @GET("phone-number")
    Call<BaseResponse<List<PhoneNumber>>> getPhoneNumbers(@Query("userId") String userId);

    @GET("phone-number/pool")
    Call<BaseResponse<List<PhoneNumber>>> getPoolPhoneNumbers();

    @POST("phone-number/pool")
    Call<BaseResponse<PhoneNumber>> buyPoolPhoneNumber(@Body PhoneNumber phoneNumber);

    @GET("phone-number/{id}")
    Call<BaseResponse<PhoneNumber>> getPhoneNumber(@Path("id") String id);

    @DELETE("phone-number/{id}")
    Call<BaseResponse<String>> deletePhoneNumber(@Path("id") String id,
                                                 @Query("account_sid") String accountSid,
                                                 @Query("auth_token") String authToken);

    @PUT("phone-number/{id}/forward")
    Call<BaseResponse<PhoneNumber>> updateForward(@Path("id") String id, @Query("forward_phone_number") String forwardPhoneNumber, @Query("forward_email") String forwardEmail);

    @PUT("phone-number/{id}/forward/enable")
    Call<BaseResponse<PhoneNumber>> enableForward(@Path("id") String id, @Query("is_forward") boolean isEnable);

    @PUT("phone-number/{id}/expire")
    Call<BaseResponse<PhoneNumber>> updateExpire(@Path("id") String id, @Query("user_id") String userId, @Query("expire") long expire);

    /*-----------------------------------------MESSAGE-----------------------------------------*/

    @FormUrlEncoded
    @POST("message/send-message")
    Call<BaseResponse<MessageItem>> createMessage(@Field("account_sid") String accountSid,
                                                  @Field("auth_token") String authToken,
                                                  @Field("userId") String userId,
                                                  @Field("from") String from,
                                                  @Field("to") String to,
                                                  @Field("body") String body);

    @GET("message/retrieve")
    Call<BaseResponse<ResourceMessage>> getMessage(@Query("account_sid") String accountSid,
                                                   @Query("auth_token") String authToken,
                                                   @Query("phone_number") String phoneNumber,
                                                   @Query("page_incoming") String pageIncoming,
                                                   @Query("page_outgoing") String pageOutgoing);

    /*-----------------------------------------CALL-----------------------------------------*/

    @FormUrlEncoded
    @POST("call/token")
    Call<BaseResponse<String>> createToken(@Field("client") String client,
                                           @Field("account_sid") String accountSid,
                                           @Field("auth_token") String authToken,
                                           @Field("application_sid") String application_sid);

    @GET("call/records")
    Call<BaseResponse<ResourceRecord>> getRecords(@Query("account_sid") String accountSid,
                                                  @Query("auth_token") String authToken,
                                                  @Query("phone_number") String phoneNumber,
                                                  @Query("page") String page);

    @GET("call/logs")
    Call<BaseResponse<ResourceCall>> getCallLogs(@Query("account_sid") String accountSid,
                                                 @Query("auth_token") String authToken,
                                                 @Query("phone_number") String phoneNumber,
                                                 @Query("page_incoming") String pageIncoming,
                                                 @Query("page_outgoing") String pageOutgoing);

    /*-----------------------------------------USAGE-----------------------------------------*/

    @GET("usage/{id}")
    Call<BaseResponse<UsageItem>> usage(@Path("id") String userId);

    /*-----------------------------------------PURCHASE-----------------------------------------*/

    @POST("purchase")
    Call<MorePhonePurchase> purchase(@Body MorePhonePurchase morePhonePurchase);


    /*-----------------------------------------PHONE REGISTER-----------------------------------------*/
    @POST("phone/register-application")
    Call<Response> registerApplication(@Query("incoming_phone_number_sid") String incomingPhoneNumberSid);

    @POST("phone/binding")
    Call<Response> binding(@Body BindingRequest bindingRequest);


    /*-----------------------------------------CONTACT-----------------------------------------*/

    @POST("contact")
    Call<BaseResponse<Contact>> createContact(@Body Contact contact);

    @DELETE("contact/{id}")
    Call<Response> deleteContact(@Path("id") String id);

    @GET("contact")
    Call<BaseResponse<List<Contact>>> loadContacts(@Query("phone_number_id") String phone_number_id);

    @GET("contact/user")
    Call<BaseResponse<List<Contact>>> loadContactsByUser(@Query("user_id") String userId);

    @PUT("contact")
    Call<BaseResponse<Contact>> updateContact(@Body Contact contact);

}
