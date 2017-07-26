package com.ethan.morephone.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.CredentialsManager;
import com.android.morephone.data.utils.TwilioManager;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.authentication.AuthenticationActivity;
import com.ethan.morephone.presentation.main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 6/27/17.
 */

public class SplashActivity extends BaseActivity {

    private Auth0 auth0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash);

        auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String accessToken = "";
        Credentials credentials = CredentialsManager.getCredentials(getApplicationContext());
        if (credentials != null) accessToken = credentials.getAccessToken();

        if (!TextUtils.isEmpty(accessToken)) {
            AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
            aClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(final UserProfile payload) {
                            if (payload != null) {

                                if (payload.getUserMetadata() != null
                                        && payload.getUserMetadata().containsKey("sid")
                                        && payload.getUserMetadata().containsKey("auth_code")) {
                                    TwilioManager.saveTwilio(getApplicationContext(), payload.getUserMetadata().get("sid").toString(), payload.getUserMetadata().get("auth_code").toString());
                                }


                                if (!MyPreference.getUserEmail(getApplicationContext()).equals(payload.getEmail())) {
                                    MyPreference.setUserEmail(getApplicationContext(), payload.getEmail());
                                    MyPreference.setUserName(getApplicationContext(), payload.getName());
                                    MyPreference.setGivenName(getApplicationContext(), payload.getGivenName());
                                    MyPreference.setNickName(getApplicationContext(), payload.getNickname());
                                    MyPreference.setUserPicture(getApplicationContext(), payload.getPictureURL());

                                    User user = User.getBuilder()
                                            .email(payload.getEmail())
                                            .token(FirebaseInstanceId.getInstance().getToken())
                                            .platform("Android")
                                            .build();
                                    DebugTool.logD("TOKEN FCM: " + FirebaseInstanceId.getInstance().getToken());

                                    ApiMorePhone.createUser(getApplicationContext(), user, new Callback<BaseResponse<User>>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                                            if (response.isSuccessful()) {
                                                BaseResponse<User> baseResponse = response.body();
                                                if (baseResponse != null && baseResponse.getResponse() != null) {
                                                    MyPreference.setUserId(getApplicationContext(), baseResponse.getResponse().getId());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse<User>> call, Throwable t) {

                                        }
                                    });
                                }
                            }

                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            CredentialsManager.deleteCredentials(getApplicationContext());
                            startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                            finish();
                        }
                    });
        } else {
            startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
            finish();
        }

    }
}
