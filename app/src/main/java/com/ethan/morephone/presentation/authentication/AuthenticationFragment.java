package com.ethan.morephone.presentation.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.authentication.login.LoginActivity;
import com.ethan.morephone.presentation.authentication.register.RegisterActivity;
import com.ethan.morephone.presentation.main.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        getFakeData(getContext());

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
                        DebugTool.logD("RESPONSE = " + response.toString());
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
        inflater.inflate(R.menu.menu_authentication, menu);
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

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

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

    public void getFakeData(final Context context) {
//        showLoading(true);
        ApiManager.fakeData(context, new Callback<FakeData>() {
            @Override
            public void onResponse(Call<FakeData> call, Response<FakeData> response) {
//                mView.showLoading(false);
                if (response.isSuccessful()) {
                    FakeData fakeData = response.body();
                    EventBus.getDefault().postSticky(fakeData);
//                    mView.showFakeData(fakeData);
//                    mView.showPhoneNumbers(fakeData.list_number);
                }
            }

            @Override
            public void onFailure(Call<FakeData> call, Throwable t) {
//                mView.showLoading(false);
            }
        });

    }
}
