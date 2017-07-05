package com.ethan.morephone.presentation.authentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.CredentialsManager;
import com.android.morephone.data.utils.TwilioManager;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ethan.morephone.R;
import com.ethan.morephone.model.UserFacebookModel;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.authentication.login.LoginActivity;
import com.ethan.morephone.presentation.authentication.register.RegisterActivity;
import com.ethan.morephone.presentation.main.MainActivity;
import com.ethan.morephone.presentation.splash.SplashActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Ethan on 1/15/17.
 */

public class AuthenticationFragment extends BaseFragment implements View.OnClickListener {

    public static AuthenticationFragment getInstance() {
        return new AuthenticationFragment();
    }

    private final int REQUEST_LOGIN = 100;
    private final int REQUEST_REGISTER = REQUEST_LOGIN + 1;

    CallbackManager callbackManager;

    private Auth0 auth0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null) baseActivity.enableActionBar(toolbar, true);
        setHasOptionsMenu(true);

        view.findViewById(R.id.button_authentication_facebook).setOnClickListener(this);
        view.findViewById(R.id.button_authentication_create_account).setOnClickListener(this);

        auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String accessToken = "";
        Credentials credentials = CredentialsManager.getCredentials(getApplicationContext());
        if (credentials != null) accessToken = credentials.getAccessToken();

        checkAccessToken(accessToken);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                DebugTool.logD("ACCESS TOKEN = " + accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        DebugTool.logD("RESPONSE = " + response.getJSONObject().toString());
                        if (response != null && response.getJSONObject() != null) {
                            Gson gson = new Gson();
                            String result = response.getJSONObject().toString();
                            UserFacebookModel userFacebookModel = gson.fromJson(result, UserFacebookModel.class);
                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                            if (userFacebookModel != null) {
                                intent.putExtra(RegisterActivity.EXTRA_EMAIL, userFacebookModel.email);
                            }
                            startActivityForResult(intent, REQUEST_REGISTER);
                        }
                        // Get facebook data from login
//                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                DebugTool.logE("CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                DebugTool.printStackTrace(exception);
                DebugTool.logE("ERROR");
            }
        });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_authentication, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.menu_login:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_LOGIN);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_authentication_facebook:
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);

//                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));
                doLogin();
                break;

            case R.id.button_authentication_create_account:
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), REQUEST_REGISTER);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN || requestCode == REQUEST_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }
    }

    private void doLogin() {
        WebAuthProvider.init(auth0)
                .withScheme("https")
                .withScope("openid offline_access")
                .start(getActivity(), callback);
    }

    private final AuthCallback callback = new AuthCallback() {
        @Override
        public void onFailure(@NonNull final Dialog dialog) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                }
            });
        }

        @Override
        public void onFailure(AuthenticationException exception) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Log In - Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(@NonNull Credentials credentials) {
            CredentialsManager.saveCredentials(getApplicationContext(), credentials);
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
//            checkAccessToken(credentials.getAccessToken());
//            DebugTool.logD("SUCCESS NOW");
//            nextActivity();
        }
    };

    private void nextActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        getActivity().finish();
    }

    private void checkAccessToken(String accessToken){
        if (!TextUtils.isEmpty(accessToken)) {
//            showProgress();
            AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
            aClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(final UserProfile payload) {
                            if (payload != null
                                    && payload.getUserMetadata() != null
                                    && payload.getUserMetadata().containsKey("sid")
                                    && payload.getUserMetadata().containsKey("auth_code")) {
                                TwilioManager.saveTwilio(getApplicationContext(), payload.getUserMetadata().get("sid").toString(), payload.getUserMetadata().get("auth_code").toString());
                            }
//                            hideProgress();
                            nextActivity();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            CredentialsManager.deleteCredentials(getApplicationContext());
//                            hideProgress();
                        }
                    });
        }
    }

}
