package com.android.morephone.data.network;

import android.content.Context;

import com.android.morephone.data.BaseUrl;
import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.Response;
import com.android.morephone.data.entity.call.ResourceCall;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.conversation.ResourceMessage;
import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.purchase.MorePhonePurchase;
import com.android.morephone.data.entity.record.ResourceRecord;
import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.entity.token.CredentialsEntity;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.CredentialsManager;
import com.android.morephone.data.utils.TwilioManager;

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
//        if (mRetrofit == null) {

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
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + accessToken)
                                .build();
                    }
                })

                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
//                            .addNetworkInterceptor((Interceptor) new StethoInterceptor())
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
//        }
        return mRetrofit;
    }

    //Singleton for ApiPath
    private static ApiMorePhonePath getApiPath(Context context) {
//        if (mApiPath == null) {
        CredentialsEntity credentials = CredentialsManager.getCredentials(context);
        String accessToken = "";
        if (credentials != null) {
            accessToken = credentials.getAccessToken();
            mApiPath = getRetrofit(context, accessToken).create(ApiMorePhonePath.class);
            DebugTool.logD("ACCESS TOKENNN : " + accessToken);
        } else {
            DebugTool.logD("ACCESS TOKEN 2: " + accessToken);
        }

//        }
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

    public static void createUser2(Context context,
                                   User user, Callback<BaseResponse<User>> callback) {
        Call<BaseResponse<User>> call = getApiPath(context).createUser(user);
        call.enqueue(callback);
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
                                     String groupId,
                                     long dateSent,
                                     String to,
                                     String from,
                                     String body,
                                     Callback<BaseResponse<List<MessageItem>>> callback) {

        Call<BaseResponse<List<MessageItem>>> call = getApiPath(context).createMessage(
                TwilioManager.getSid(context),
                TwilioManager.getAuthCode(context),
                userId,
                groupId,
                dateSent,
                from,
                to,
                body);
        call.enqueue(callback);
    }

    public static BaseResponse<ResourceMessage> getMessage(Context context,
                                                           String phoneNumber,
                                                           String phoneNumberId,
                                                           String pageIncoming,
                                                           String pageOutgoing) {
        Call<BaseResponse<ResourceMessage>> call = getApiPath(context).getMessage(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber, phoneNumberId, pageIncoming, pageOutgoing);
        try {
            return call.execute().body();
        } catch (Exception e) {
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

    public static BaseResponse<ResourceRecord> getRecords(Context context,
                                                          String phoneNumber,
                                                          String page) {
        Call<BaseResponse<ResourceRecord>> call = getApiPath(context).getRecords(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber, page);
        try {
            return call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BaseResponse<ResourceCall> getCallLogs(Context context,
                                                         String phoneNumber,
                                                         String pageIncoming,
                                                         String pageOutgoing) {
        Call<BaseResponse<ResourceCall>> call = getApiPath(context).getCallLogs(TwilioManager.getSid(context), TwilioManager.getAuthCode(context), phoneNumber, pageIncoming, pageOutgoing);
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


    /*-----------------------------------------CONTACT-----------------------------------------*/

    public static void createContact(Context context,
                                     Contact contact,
                                     Callback<BaseResponse<Contact>> callback) {
        Call<BaseResponse<Contact>> call = getApiPath(context).createContact(contact);
        call.enqueue(callback);
    }

    public static void deleteContact(Context context,
                                     String id,
                                     Callback<Response> callback) {
        Call<Response> call = getApiPath(context).deleteContact(id);
        call.enqueue(callback);
    }

    public static void loadContacts(Context context,
                                    String phoneNumberId,
                                    Callback<BaseResponse<List<Contact>>> callback) {
        Call<BaseResponse<List<Contact>>> call = getApiPath(context).loadContacts(phoneNumberId);
        call.enqueue(callback);
    }

    public static void updateContact(Context context,
                                     Contact contact,
                                     Callback<BaseResponse<Contact>> callback) {
        Call<BaseResponse<Contact>> call = getApiPath(context).updateContact(contact);
        call.enqueue(callback);
    }

    /*-----------------------------------------GROUP-----------------------------------------*/

    public static void createGroup(Context context,
                                   Group group,
                                   Callback<BaseResponse<Group>> callback) {
        Call<BaseResponse<Group>> call = getApiPath(context).createGroup(group);
        call.enqueue(callback);
    }

    public static void deleteGroup(Context context,
                                   String id,
                                   Callback<Response> callback) {
        Call<Response> call = getApiPath(context).deleteGroup(id);
        call.enqueue(callback);
    }

    public static void loadGroupByUser(Context context,
                                       String phoneNumberId,
                                       Callback<BaseResponse<List<Group>>> callback) {
        Call<BaseResponse<List<Group>>> call = getApiPath(context).loadGroupByUser(phoneNumberId);
        call.enqueue(callback);
    }

    public static void updateGroup(Context context,
                                   Group group,
                                   Callback<BaseResponse<Group>> callback) {
        Call<BaseResponse<Group>> call = getApiPath(context).updateGroup(group);
        call.enqueue(callback);
    }
}
