package com.android.morephone.data.network;


import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.entity.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by AnPEthan on 8/11/2016.
 */
interface ApiMorePhonePath {

    /*-----------------------------------------USER-----------------------------------------*/
    @POST("api/v1/user")
    Call<BaseResponse<User>> createUser(@Body User user);

    @PUT("api/v1/user/{id}/token")
    Call<BaseResponse<User>> updateFcmToken(@Path("id") String id, @Query("token") String token);

    /*-----------------------------------------PHONE NUMBER-----------------------------------------*/

    @POST("api/v1/phone-number")
    Call<BaseResponse<PhoneNumber>> createPhoneNumber(@Body PhoneNumber user);



    /*-----------------------------------------PHONE REGISTER-----------------------------------------*/
    @POST("phone/register-application")
    Call<Response> registerApplication(@Query("incoming_phone_number_sid") String incomingPhoneNumberSid);

    @POST("phone/binding")
    Call<Response> binding(@Body BindingRequest bindingRequest);

}
