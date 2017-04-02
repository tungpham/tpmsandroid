package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.entity.phonenumbers.CountryCode;
import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.entity.twilio.voice.VoiceListResourceResponse;

import java.io.IOException;
import java.util.List;
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
 * Created by AnPEthan on 7/8/2016.
 */
public class ApiManager {

//    private static final String BASE_URL = "https://raw.githubusercontent.com/tungpham/tpmsservices/";
    private static final String BASE_URL = "https://api.twilio.com/2010-04-01/";

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
                            .authenticator(new Authenticator() {
                                @Override
                                public Request authenticate(Route route, Response response) throws IOException {
                                    System.out.println("Authenticating for response: " + response);
                                    System.out.println("Challenges: " + response.challenges());
                                    String credential = Credentials.basic("ACebd7d3a78e2fdda9e51239bad6b09f97", "8d2af0937ed2a581dbb19f70dd1dd43b");
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
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<MessageItem> call = getApiPath(context).createMessage(accountsid, from, to, body);
        call.enqueue(callback);
    }

    public static void getAllMessages(Context context,
                                      Callback<MessageListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<MessageListResourceResponse> call = getApiPath(context).getAllMessageListResource(accountsid);
        call.enqueue(callback);
    }

    public static void getMessages(Context context,
                                   String phoneNumberIncoming,
                                   String phoneNumberOutgoing,
                                   Callback<MessageListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<MessageListResourceResponse> call = getApiPath(context).getMessages(accountsid, phoneNumberIncoming, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void getMessagesIncoming(Context context,
                                           String phoneNumberIncoming,
                                           Callback<MessageListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<MessageListResourceResponse> call = getApiPath(context).getMessagesIncoming(accountsid, phoneNumberIncoming);
        call.enqueue(callback);
    }

    public static void getMessagesOutgoing(Context context,
                                           String phoneNumberOutgoing,
                                           Callback<MessageListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<MessageListResourceResponse> call = getApiPath(context).getMessagesOutgoing(accountsid, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void deleteMessage(Context context, String messagesid) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<Void> call = getApiPath(context).deleteMessage(accountsid, messagesid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void createVoice(Context context,
                                   String phoneNumberOutgoing,
                                   String phoneNumberIncoming,
                                   String applicationSid,
                                   String sipAuthUsername,
                                   String sipAuthPassword,
                                   Callback<VoiceItem> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<VoiceItem> call = getApiPath(context).createVoice(accountsid, phoneNumberOutgoing, phoneNumberIncoming, applicationSid, sipAuthUsername, sipAuthPassword);
        call.enqueue(callback);
    }

    public static void getAllVoice(Context context,
                                   Callback<VoiceListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<VoiceListResourceResponse> call = getApiPath(context).getAllVoiceListResource(accountsid);
        call.enqueue(callback);
    }

    public static void getVoices(Context context,
                                 String phoneNumberIncoming,
                                 String phoneNumberOutgoing,
                                 Callback<VoiceListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<VoiceListResourceResponse> call = getApiPath(context).getVoices(accountsid, phoneNumberIncoming, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void getVoicesIncoming(Context context,
                                         String phoneNumberIncoming,
                                         Callback<VoiceListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<VoiceListResourceResponse> call = getApiPath(context).getVoiceByIncoming(accountsid, phoneNumberIncoming);
        call.enqueue(callback);
    }

    public static void getVoicesOutgoing(Context context,
                                         String phoneNumberOutgoing,
                                         Callback<VoiceListResourceResponse> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<VoiceListResourceResponse> call = getApiPath(context).getVoicesOutgoing(accountsid, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void deleteVoice(Context context, String callsid) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<Void> call = getApiPath(context).deleteVoice(accountsid, callsid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void fakeData(Context context, Callback<FakeData> callback) {
        Call<FakeData> call = getApiPath(context).getFakeDAta();
        call.enqueue(callback);
    }

    public static void getCountryCode(Context context, Callback<List<CountryCode>> callback) {
        Call<List<CountryCode>> call = getApiPath(context).getCountryCode();
        call.enqueue(callback);
    }

    public static void getAvailableCountries(Context context, Callback<AvailableCountries> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<AvailableCountries> call = getApiPath(context).getAvailableCountries(accountsid);
        call.enqueue(callback);
    }

    public static void getAvailablePhoneNumbers(Context context,
                                                String countryCode,
                                                Callback<AvailablePhoneNumbers> callback) {
        String accountsid = "ACebd7d3a78e2fdda9e51239bad6b09f97";
        Call<AvailablePhoneNumbers> call = getApiPath(context).getAvailablePhoneNumbers(accountsid, countryCode);
        call.enqueue(callback);
    }
}
