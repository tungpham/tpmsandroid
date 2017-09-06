package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.entity.MessageItem;
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
 * Created by truongnguyen on 9/6/17.
 */

public class Auth0Service {

    private static final String BASE_URL = "https://api.twilio.com/2010-04-01/";

    private static final int PAGE_SIZE = 50;

    private static ApiPath mApiPath;

    private static volatile Retrofit mRetrofit;

    private static final String TAG = ApiManager.class.getSimpleName();

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
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(logging)
//                            .addNetworkInterceptor((Interceptor) new StethoInterceptor())
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
    private static ApiPath getApiPath(Context context) {
        if (mApiPath == null) {
            synchronized (ApiManager.class) {
                if (mApiPath == null) {
                    mApiPath = getRetrofit(context).create(ApiPath.class);
                }
            }
        }
        return mApiPath;
    }

    public static void createMessage(Context context,
                                     String to,
                                     String from,
                                     String body,
                                     Callback<MessageItem> callback) {
        Call<MessageItem> call = getApiPath(context).createMessage(TwilioManager.getSid(context), from, to, body);
        call.enqueue(callback);
    }

}
