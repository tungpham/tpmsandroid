package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.utils.TwilioManager;

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

    private static final int PAGE_SIZE = 5;

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

    public static void getAllMessages(Context context,
                                      Callback<MessageListResourceResponse> callback) {
        Call<MessageListResourceResponse> call = getApiPath(context).getAllMessageListResource(TwilioManager.getSid(context));
        call.enqueue(callback);
    }

    public static void getMessages(Context context,
                                   String phoneNumberIncoming,
                                   String phoneNumberOutgoing,
                                   Callback<MessageListResourceResponse> callback) {
        Call<MessageListResourceResponse> call = getApiPath(context).getMessages(TwilioManager.getSid(context), phoneNumberIncoming, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void getMessagesIncoming(Context context,
                                           String phoneNumberIncoming,
                                           Callback<MessageListResourceResponse> callback) {
        Call<MessageListResourceResponse> call = getApiPath(context).getMessagesIncoming(TwilioManager.getSid(context), phoneNumberIncoming);
        call.enqueue(callback);
    }

    public static void getMessagesOutgoing(Context context,
                                           String phoneNumberOutgoing,
                                           Callback<MessageListResourceResponse> callback) {
        Call<MessageListResourceResponse> call = getApiPath(context).getMessagesOutgoing(TwilioManager.getSid(context), phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void deleteMessage(Context context, String messagesid) {
        Call<Void> call = getApiPath(context).deleteMessage(TwilioManager.getSid(context), messagesid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    /*-----------------------------------------VOICE-----------------------------------------*/

    public static void createVoice(Context context,
                                   String phoneNumberOutgoing,
                                   String phoneNumberIncoming,
                                   String applicationSid,
                                   String sipAuthUsername,
                                   String sipAuthPassword,
                                   Callback<com.android.morephone.data.entity.call.Call> callback) {
        Call<com.android.morephone.data.entity.call.Call> call = getApiPath(context).createVoice(TwilioManager.getSid(context), phoneNumberOutgoing, phoneNumberIncoming, applicationSid, sipAuthUsername, sipAuthPassword);
        call.enqueue(callback);
    }

    public static void getAllCalls(Context context,
                                   Callback<Calls> callback) {
        Call<Calls> call = getApiPath(context).getAllCalls(TwilioManager.getSid(context));
        call.enqueue(callback);
    }

    public static void getCalls(Context context,
                                String phoneNumberIncoming,
                                String phoneNumberOutgoing,
                                Callback<Calls> callback) {
        Call<Calls> call = getApiPath(context).getCalls(TwilioManager.getSid(context), phoneNumberIncoming, phoneNumberOutgoing);
        call.enqueue(callback);
    }

    public static void getCall(Context context,
                                String callSid,
                                Callback<com.android.morephone.data.entity.call.Call> callback) {
        Call<com.android.morephone.data.entity.call.Call> call = getApiPath(context).getCall(TwilioManager.getSid(context), callSid);
        call.enqueue(callback);
    }

    public static void getCallsIncoming(Context context,
                                        String phoneNumberIncoming,
                                        int page,
                                        Callback<Calls> callback) {
        Call<Calls> call = getApiPath(context).getCallsIncoming(TwilioManager.getSid(context), phoneNumberIncoming, PAGE_SIZE, page);
        call.enqueue(callback);
    }

    public static void getCallsOutgoing(Context context,
                                        String phoneNumberOutgoing,
                                        int page,
                                        Callback<Calls> callback) {
        Call<Calls> call = getApiPath(context).getCallsOutgoing(TwilioManager.getSid(context), phoneNumberOutgoing, PAGE_SIZE, page);
        call.enqueue(callback);
    }

    public static void deleteVoice(Context context, String callsid) {
        Call<Void> call = getApiPath(context).deleteCall(TwilioManager.getSid(context), callsid);
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


    /*-----------------------------------------INCOMING PHONE NUMBER -----------------------------------------*/

    public static void getCountryCode(Context context, Callback<List<AvailableCountry>> callback) {
        Call<List<AvailableCountry>> call = getApiPath(context).getCountryCode();
        call.enqueue(callback);
    }

    public static void getAvailableCountries(Context context, Callback<AvailableCountries> callback) {
        Call<AvailableCountries> call = getApiPath(context).getAvailableCountries(TwilioManager.getSid(context));
        call.enqueue(callback);
    }

    public static void getAvailablePhoneNumbers(Context context,
                                                String countryCode,
                                                String contains,
                                                boolean smsEnabled,
                                                boolean mmsEnabled,
                                                boolean voiceEnabled,
                                                Callback<AvailablePhoneNumbers> callback) {
        Call<AvailablePhoneNumbers> call = getApiPath(context).getAvailablePhoneNumbers(TwilioManager.getSid(context), countryCode, contains, smsEnabled, mmsEnabled, voiceEnabled);
        call.enqueue(callback);
    }

    public static void getIncomingPhoneNumbers(Context context,
                                               Callback<IncomingPhoneNumbers> callback) {
        Call<IncomingPhoneNumbers> call = getApiPath(context).getIncomingPhoneNumbers(TwilioManager.getSid(context));
        call.enqueue(callback);
    }

    public static void deleteIncomingPhoneNumber(Context context, String accountsid, String incomingPhoneNumberSid) {
//        Call<Void> call = getApiPath(context).deletePhoneNumber(accountsid, incomingPhoneNumberSid);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//            }
//        });
    }

    public static void changeFriendlyName(Context context,
                                          String accountSid,
                                          String incomingPhoneNumberSid,
                                          String friendlyName,
                                          Callback<IncomingPhoneNumber> callback) {
        Call<IncomingPhoneNumber> call = getApiPath(context).changeFriendlyName(TwilioManager.getSid(context), incomingPhoneNumberSid, friendlyName);
        call.enqueue(callback);
    }

    public static void buyIncomingPhoneNumber(Context context,
                                          String phoneNumber,
                                          Callback<IncomingPhoneNumber> callback) {
        Call<IncomingPhoneNumber> call = getApiPath(context).buyIncomingPhoneNumber(TwilioManager.getSid(context), phoneNumber);
        call.enqueue(callback);
    }

    /*-----------------------------------------RECORDINGS-----------------------------------------*/

    public static void getRecordListResource(Context context,
                                             String accountSid,
                                             String callSid,
                                             Callback<RecordListResourceResponse> callback) {
        Call<RecordListResourceResponse> call = getApiPath(context).getRecordListResource(TwilioManager.getSid(context), callSid);
        call.enqueue(callback);
    }

    public static void getRecords(Context context,
                                  Callback<RecordListResourceResponse> callback) {
        Call<RecordListResourceResponse> call = getApiPath(context).getRecords(TwilioManager.getSid(context));
        call.enqueue(callback);
    }

    public static void deleteRecord(Context context,
                                    String accountSid,
                                    String recordSid) {
        Call<Void> call = getApiPath(context).deleteRecord(TwilioManager.getSid(context), recordSid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void deleteCallRecord(Context context,
                                        String accountSid,
                                        String callSid,
                                        String recordSid) {
        Call<Void> call = getApiPath(context).deleteCallRecoding(TwilioManager.getSid(context), callSid, recordSid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


}
