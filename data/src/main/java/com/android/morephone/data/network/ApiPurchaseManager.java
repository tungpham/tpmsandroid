package com.android.morephone.data.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ethan on 7/5/17.
 */

public class ApiPurchaseManager {

    private static final String BASE_URL = "http://warm-atoll-19305.herokuapp.com/";

    private static ApiPurchasePath mApiPath;

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
//                            .authenticator(new Authenticator() {
//                                @Override
//                                public Request authenticate(Route route, Response response) throws IOException {
//                                    String credential = Credentials.basic(TwilioManager.getSid(context), TwilioManager.getAuthCode(context));
//                                    return response.request().newBuilder()
//                                            .header("Authorization", credential)
//                                            .build();
//                                }
//                            })
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
    private static ApiPurchasePath getApiPath(Context context) {
        if (mApiPath == null) {
            synchronized (ApiPurchaseManager.class) {
                if (mApiPath == null) {
                    mApiPath = getRetrofit(context).create(ApiPurchasePath.class);
                }
            }
        }
        return mApiPath;
    }

//    public static void purchase(Context context,
//                                  MorePhonePurchase morePhonePurchase,
//                                  Callback<MorePhonePurchase> callback) {
//        Call<MorePhonePurchase> call = getApiPath(context).purchase(morePhonePurchase);
//        call.enqueue(callback);
//    }
}
