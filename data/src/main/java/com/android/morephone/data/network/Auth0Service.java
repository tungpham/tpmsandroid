package com.android.morephone.data.network;

import android.content.Context;
import android.util.Base64;

import com.android.morephone.data.BaseUrl;
import com.android.morephone.data.entity.token.CredentialsEntity;
import com.android.morephone.data.entity.token.TokenRequest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by truongnguyen on 9/6/17.
 */

public class Auth0Service {

    private static final String BASE_URL = "https://coderdaudat.auth0.com/";

    private static final int PAGE_SIZE = 50;

    private static Auth0Path mApiPath;

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
    private static Auth0Path getApiPath(Context context) {
        if (mApiPath == null) {
            synchronized (ApiManager.class) {
                if (mApiPath == null) {
                    mApiPath = getRetrofit(context).create(Auth0Path.class);
                }
            }
        }
        return mApiPath;
    }

    public static void authorize(Context context, Callback<String> callback) {
        Call<String> call = getApiPath(context).authorize(BaseUrl.API_IDENTIFIER,
                BaseUrl.SCOPE,
                BaseUrl.RESPONSE_TYPE,
                BaseUrl.CLIENT_ID,
                BaseUrl.REDIRECT_URI,
                createCodeChallenge(createCodeVerifier()),
                BaseUrl.CODE_CHALLENGE_METHOD);
        call.enqueue(callback);
    }

    public static void token(Context context, String code, Callback<CredentialsEntity> callback) {
        TokenRequest tokenRequest = new TokenRequest(BaseUrl.GRANT_TYPE, BaseUrl.CLIENT_ID, BaseUrl.CODE_VERIFIER, code, BaseUrl.REDIRECT_URI);
        Call<CredentialsEntity> call = getApiPath(context).token(tokenRequest);
        call.enqueue(callback);
    }

    private static String createCodeVerifier(){
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return Base64.encodeToString(code, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }

    private static String createCodeChallenge(String verifier){
        byte[] bytes = new byte[0];
        try {
            bytes = verifier.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }


}
