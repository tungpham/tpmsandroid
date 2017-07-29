package com.android.morephone.data.network;


import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.purchase.MorePhonePurchase;
import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.entity.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<BaseResponse<PhoneNumber>> createPhoneNumber(@Body PhoneNumber user);


    /*-----------------------------------------MESSAGE-----------------------------------------*/

    @FormUrlEncoded
    @POST("message/send-message")
    Call<BaseResponse<MessageItem>> createMessage(@Field("userId") String userId,
                                    @Field("from") String from,
                                    @Field("to") String to,
                                    @Field("body") String body);

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



}
