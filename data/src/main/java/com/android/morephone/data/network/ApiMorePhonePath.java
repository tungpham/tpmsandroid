package com.android.morephone.data.network;


import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.register.BindingRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by AnPEthan on 8/11/2016.
 */
interface ApiMorePhonePath {

    /*-----------------------------------------CALL LOGS-----------------------------------------*/
    @POST("phone/register-application")
    Call<Response> registerApplication(@Query("incoming_phone_number_sid") String incomingPhoneNumberSid);

    @POST("phone/binding")
    Call<Response> binding(@Body BindingRequest bindingRequest);

}
