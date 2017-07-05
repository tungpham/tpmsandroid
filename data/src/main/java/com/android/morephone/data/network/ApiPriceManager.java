package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.entity.price.PricePhoneNumber;
import com.android.morephone.data.utils.TwilioManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ethan on 7/5/17.
 */

public class ApiPriceManager {

    private static final String BASE_URL = "https://pricing.twilio.com/v1/";

    private static ApiPricePath mApiPath;

    private static volatile Retrofit mRetrofit;

    //Singleton for Retrofit
    private static Retrofit getRetrofit(final Context context) {
        if (mRetrofit == null) {
            synchronized (ApiManager.class) {
                if (mRetrofit == null) {

                    //Set log
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    boolean isLog = true;
                    logging.setLevel(isLog ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                    //Create cache
//                    File file = new File(context.getCacheDir(), "response");

                    //Add log and set time out
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .authenticator(new Authenticator() {
                                @Override
                                public Request authenticate(Route route, Response response) throws IOException {
                                    String credential = Credentials.basic(TwilioManager.getSid(context), TwilioManager.getAuthCode(context));
                                    return response.request().newBuilder()
                                            .header("Authorization", credential)
                                            .build();
                                }
                            })
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(logging)
                            .build();
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient).build();
                }
            }
        }
        return mRetrofit;
    }


    //Singleton for ApiPath
    private static ApiPricePath getApiPath(Context context) {
        if (mApiPath == null) {
            synchronized (ApiPriceManager.class) {
                if (mApiPath == null) {
                    mApiPath = getRetrofit(context).create(ApiPricePath.class);
                }
            }
        }
        return mApiPath;
    }

    public static void getPrice(Context context,
                                     Callback<PricePhoneNumber> callback) {
        Call<PricePhoneNumber> call = getApiPath(context).getPrice("US");
        call.enqueue(callback);
    }
}
