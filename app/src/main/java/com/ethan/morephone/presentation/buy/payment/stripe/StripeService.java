package com.ethan.morephone.presentation.buy.payment.stripe;

import android.database.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ethan on 5/26/17.
 */

public interface StripeService {

    // For simplicity, we have URL encoded our body data, but your code will likely
    // want a model class send up as JSON
    @FormUrlEncoded
    @POST("create_charge")
    Observable<Void> createQueryCharge(
            @Field("amount") long amount,
            @Field("source") String source);
}
