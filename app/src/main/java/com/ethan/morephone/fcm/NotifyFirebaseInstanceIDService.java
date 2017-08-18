package com.ethan.morephone.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.presentation.record.TestVoiceActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "NotifyFbIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DebugTool.logD( "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        Intent intent = new Intent(TestVoiceActivity.ACTION_FCM_TOKEN);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        /*
         * Use the service to refresh the registration binding. The token is not passed
         * because the token is requested by the Service.
         */

        if (!TextUtils.isEmpty(MyPreference.getUserId(getApplicationContext()))) {
            ApiMorePhone.updateFcmToken(getApplicationContext(), MyPreference.getUserId(getApplicationContext()), token, new Callback<BaseResponse<User>>() {
                @Override
                public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            DebugTool.logD("FCM TOKEN UPDATE SUCCESS");
                        } else {
                            DebugTool.logD("FCM TOKEN UPDATE ERROR");
                        }
                    } else {
                        DebugTool.logD("FCM TOKEN UPDATE ERROR");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                    DebugTool.logD("FCM TOKEN UPDATE ERROR");
                }
            });
        } else {
            DebugTool.logD("USER NOT REGISTER");
        }
    }

}
