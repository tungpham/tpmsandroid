package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.record.Records;
import com.android.morephone.data.entity.usage.Usage;
import com.stormpath.sdk.Stormpath;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AnPEthan on 7/8/2016.
 */
public class ApiMorePhone {

    //    private static final String BASE_URL = "https://raw.githubusercontent.com/tungpham/tpmsservices/";
    private static final String BASE_URL = "https://private-091c0-morephone.apiary-mock.com/";

    private static ApiMorePhonePath mApiPath;

    private static volatile Retrofit mRetrofit;

    private static final String TAG = ApiMorePhone.class.getSimpleName();

    //Singleton for Retrofit
    private static Retrofit getRetrofit(final Context context) {
        if (mRetrofit == null) {
            synchronized (ApiMorePhone.class) {
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
////                                    System.out.println("Authenticating for response: " + response);
////                                    System.out.println("Challenges: " + response.challenges());
//                                    String credential = Credentials.basic("ACebd7d3a78e2fdda9e51239bad6b09f97", "8d2af0937ed2a581dbb19f70dd1dd43b");
//                                    return response.request().newBuilder()
//                                            .header("Authorization", credential)
//                                            .build();
//                                }
//                            })
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder ongoing = chain.request().newBuilder();
                                    ongoing.addHeader("Accept", "application/json");
                                    ongoing.addHeader("Authorization", "Bearer " + Stormpath.getAccessToken());
                                    return chain.proceed(ongoing.build());
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
    private static ApiMorePhonePath getApiPath(Context context) {
        if (mApiPath == null) {
            synchronized (ApiMorePhone.class) {
                if (mApiPath == null) {
                    mApiPath = getRetrofit(context).create(ApiMorePhonePath.class);
                }
            }
        }
        return mApiPath;
    }

    public static void getCallLogs(Context context,
                                   String accountSid,
                                   String phoneNumber,
                                   Callback<Calls> callback) {
        Call<Calls> call = getApiPath(context).getCallLogs();
        call.enqueue(callback);
    }

    public static void getRecords(Context context,
                                  String accountSid,
                                  String phoneNumber,
                                  Callback<Records> callback) {
        Call<Records> call = getApiPath(context).getRecords();
        call.enqueue(callback);
    }

    public static void getUsage(Context context,
                                String accountSid,
                                String phoneNumber,
                                Callback<Usage> callback) {
        Call<Usage> call = getApiPath(context).getUsage();
        call.enqueue(callback);
    }
}
