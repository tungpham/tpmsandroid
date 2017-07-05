package com.android.morephone.data.network;

import com.android.morephone.data.entity.price.PricePhoneNumber;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Ethan on 7/5/17.
 */

public interface ApiPricePath {

    @GET("PhoneNumbers/Countries/{country}.json")
    Call<PricePhoneNumber> getPrice(@Path("country") String country);

}
