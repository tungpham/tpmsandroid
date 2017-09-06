package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.BaseUrl;
import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.purchase.MorePhonePurchase;
import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.CredentialsManager;
import com.android.morephone.data.utils.TwilioManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Credentials;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
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

    private static ApiMorePhonePath mApiPath;

    private static volatile Retrofit mRetrofit;

    private static final String TAG = ApiMorePhone.class.getSimpleName();

    //Singleton for Retrofit
    private static Retrofit getRetrofit(final Context context, final String accessToken) {
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
                            .authenticator(new Authenticator() {
                                @Override
                                public Request authenticate(Route route, okhttp3.Response response) throws IOException {
//                                    System.out.println("Authenticating for response: " + response);
//                                    System.out.println("Challenges: " + response.challenges());
                                    return response.request().newBuilder()
                                            .header("Authorization", "Bearer " + accessToken)
                                            .build();
                                }
                            })
//                            .addInterceptor(new Interceptor() {
//                                @Override
//                                public Response intercept(Chain chain) throws IOException {
//                                    Request.Builder ongoing = chain.request().newBuilder();
//                                    ongoing.addHeader("Accept", "application/json");
//                                    return chain.proceed(ongoing.build());
//                                }
//                            })
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(logging)
//                            .addNetworkInterceptor((Interceptor) new StethoInterceptor())
                            .build();

                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BaseUrl.BASE_URL)
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
                    com.auth0.android.result.Credentials credentials = CredentialsManager.getCredentials(context);
                    String accessToken = "";
                    if (credentials != null) {
                        accessToken = credentials.getAccessToken();
                        DebugTool.logD("ACCESS TOKEN: " + accessToken);
                    }
                    mApiPath = getRetrofit(context, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik9URTVOVVJFTmpaRlFrWTVSVFEzTmpsRk1UQkJRakpFTVVVek9VRXlRamd3TkRoQ1FUTTJNdyJ9.eyJpc3MiOiJodHRwczovL2NvZGVyZGF1ZGF0LmF1dGgwLmNvbS8iLCJzdWIiOiJ3Tk1WNGJqNXZFYmVUUUJ2eGJWR2dyMUhWazRzOG9MWkBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly90cG1zc2VydmljZS5oZXJva3VhcHAuY29tIiwiZXhwIjoxNTA0NzcwMjc0LCJpYXQiOjE1MDQ2ODM4NzQsInNjb3BlIjoid3JpdGU6cGhvbmUtbnVtYmVyIGRlbGV0ZTpwaG9uZS1udW1iZXIgd3JpdGU6cG9vbC1waG9uZS1udW1iZXIgcmVhZDpwb29sLXBob25lLW51bWJlcnMgcmVhZDpwaG9uZS1udW1iZXJzIHJlYWQ6cGhvbmUtbnVtYmVyIHdyaXRlOmZvcndhcmQtcGhvbmUtbnVtYmVyIHdyaXRlOmV4cGlyZS1waG9uZS1udW1iZXIgd3JpdGU6dXNlciB3cml0ZTp1c2VyLXRva2VuIHdyaXRlOmNhbGwtdG9rZW4gcmVhZDpyZWNvcmRzIHJlYWQ6Y2FsbC1sb2dzIHdyaXRlOnNlbmQtbWVzc2FnZSByZWFkOm1lc3NhZ2VzIHJlYWQ6dXNhZ2Ugd3JpdGU6cHVyY2hhc2UifQ.EvToj44_ObsYvhdMuJVe1akkudLDlQwN4XtmawOjaRaG3t5lKLBHSrBQ_xFXVYqgkdenFLjJwkQED2H-TZpU0hKrasCgg0x0cpj2OJiaRVj1kBcF3x56xDJV6h175SM7WT5ejMnHhitKHT0vny_IMF0xIMeeC3QrLM_ERLS0PiP1yNib--vPM7GAt4C2vcVFUpavk233wYrqeL7cjx_uv7qLK-yokel320da9crff5mNFj0Ii_ddEFrlE5Q4eU8MQ5Dk9cFtJENNWJtHKotRw6hDG5wVB9yxgZ_2QNv9IzQYLyfI8iYrxEMgnOP5pfGf39j76ngN3FskisPV6Q0ylw").create(ApiMorePhonePath.class);
                }
            }
        }
        return mApiPath;
    }


    /*-----------------------------------------USER-----------------------------------------*/

    public static BaseResponse<User> createUser(Context context,
                                                User user) {
        Call<BaseResponse<User>> call = getApiPath(context).createUser(user);
        try {
            BaseResponse<User> baseResponse = call.execute().body();
            return baseResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateFcmToken(Context context,
                                      String id,
                                      String token,
                                      Callback<BaseResponse<User>> callback) {
        Call<BaseResponse<User>> call = getApiPath(context).updateFcmToken(id, token);
        call.enqueue(callback);
    }


    public static void updateForward(Context context,
                                     String id,
                                     String forwardPhoneNumber,
                                     String forwardEmail,
                                     Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).updateForward(id, forwardPhoneNumber, forwardEmail);
        call.enqueue(callback);
    }

    public static void enableForward(Context context,
                                     String id,
                                     boolean isForward,
                                     Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).enableForward(id, isForward);
        call.enqueue(callback);
    }

    /*-----------------------------------------PHONE NUMBER-----------------------------------------*/

    public static void createPhoneNumber(Context context,
                                         PhoneNumber phoneNumber,
                                         Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).createPhoneNumber(phoneNumber);
        call.enqueue(callback);
    }

    public static void getPhoneNumbers(Context context,
                                       String userId,
                                       Callback<BaseResponse<List<PhoneNumber>>> callback) {
        Call<BaseResponse<List<PhoneNumber>>> call = getApiPath(context).getPhoneNumbers(userId);
        call.enqueue(callback);
    }

    public static void getPhoneNumber(Context context,
                                      String id,
                                      Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).getPhoneNumber(id);
        call.enqueue(callback);
    }

    public static void buyPoolPhoneNumber(Context context,
                                          PhoneNumber phoneNumber,
                                          Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).buyPoolPhoneNumber(phoneNumber);
        call.enqueue(callback);
    }

    public static void getPoolPhoneNumber(Context context,
                                          Callback<BaseResponse<List<PhoneNumber>>> callback) {
        Call<BaseResponse<List<PhoneNumber>>> call = getApiPath(context).getPoolPhoneNumbers();
        call.enqueue(callback);
    }

    public static void deletePhoneNumber(Context context,
                                         String id,
                                         String accountToken,
                                         String authToken,
                                         Callback<BaseResponse<String>> callback) {
        Call<BaseResponse<String>> call = getApiPath(context).deletePhoneNumber(id, accountToken, authToken);
        call.enqueue(callback);
    }

    public static void updateExpire(Context context,
                                    String id,
                                    String userId,
                                    long expire,
                                    Callback<BaseResponse<PhoneNumber>> callback) {
        Call<BaseResponse<PhoneNumber>> call = getApiPath(context).updateExpire(id, userId, expire);
        call.enqueue(callback);
    }

     /*-----------------------------------------PURCHASE----------------------------------------*/

    public static void purchase(Context context,
                                MorePhonePurchase morePhonePurchase,
                                Callback<MorePhonePurchase> callback) {
        Call<MorePhonePurchase> call = getApiPath(context).purchase(morePhonePurchase);
        call.enqueue(callback);
    }


    /*-----------------------------------------MESSAGE----------------------------------------*/

    public static void createMessage(Context context,
                                     String userId,
                                     String to,
                                     String from,
                                     String body,
                                     Callback<BaseResponse<MessageItem>> callback) {
        Call<BaseResponse<MessageItem>> call = getApiPath(context).createMessage(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), userId, from, to, body);
        call.enqueue(callback);
    }

    public static BaseResponse<List<ConversationModel>> getMessage(Context context,
                                                                   String phoneNumber) {
        Call<BaseResponse<List<ConversationModel>>> call = getApiPath(context).getMessage(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber);
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*-----------------------------------------CALL----------------------------------------*/

    public static void createToken(Context context,
                                   String client,
                                   Callback<BaseResponse<String>> callback) {
        Call<BaseResponse<String>> call = getApiPath(context).createToken(client, TwilioManager.getSid(context), TwilioManager.getAuthCode(context), TwilioManager.getApplicationSid(context));
        call.enqueue(callback);
    }

    public static BaseResponse<List<Record>> getRecords(Context context,
                                                        String phoneNumber) {
        Call<BaseResponse<List<Record>>> call = getApiPath(context).getRecords(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber);
        try {
            return call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BaseResponse<List<com.android.morephone.data.entity.call.Call>> getCallLogs(Context context,
                                                                                              String phoneNumber) {
        Call<BaseResponse<List<com.android.morephone.data.entity.call.Call>>> call = getApiPath(context).getCallLogs(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber);
        try {
            return call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

     /*-----------------------------------------USAGE----------------------------------------*/

    public static void usage(Context context,
                             String userId,
                             Callback<BaseResponse<UsageItem>> callback) {
        Call<BaseResponse<UsageItem>> call = getApiPath(context).usage(userId);
        call.enqueue(callback);
    }


    /*-----------------------------------------PHONE REGISTER-----------------------------------------*/

    public static void registerApplication(Context context,
                                           String incomingPhoneNumberSid,
                                           Callback<Response> callback) {
        Call<Response> call = getApiPath(context).registerApplication(incomingPhoneNumberSid);
        call.enqueue(callback);
    }

    public static void binding(Context context,
                               BindingRequest bindingRequest,
                               Callback<Response> callback) {
        Call<Response> call = getApiPath(context).binding(bindingRequest);
        call.enqueue(callback);
    }

}
