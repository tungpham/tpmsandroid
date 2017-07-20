package com.android.morephone.data.network;

import com.android.morephone.data.entity.purchase.MorePhonePurchase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ethan on 7/5/17.
 */

public interface ApiPurchasePath {

    @POST("api/purchase")
    Call<MorePhonePurchase> purchase(@Body MorePhonePurchase morePhonePurchase);
}
