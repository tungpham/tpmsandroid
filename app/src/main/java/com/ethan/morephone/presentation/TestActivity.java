package com.ethan.morephone.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Ethan on 6/6/17.
 */

public class TestActivity extends BaseActivity {

    private final String SERVER_TOKEN_URL = "http://3c3ef143.ngrok.io/token";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveAccessTokenfromServer();
    }

    private void retrieveAccessTokenfromServer() {
//        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        String tokenURL = SERVER_TOKEN_URL + "?device=" + deviceId;

//        Ion.with(this)
//                .load(tokenURL)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        if (e == null) {
//                            String identity = result.get("identity").getAsString();
//                            String accessToken = result.get("token").getAsString();
//                            DebugTool.logD("identity: " + identity);
//                            DebugTool.logD("accessToken: " + accessToken);
//
//                        } else {
//                            Toast.makeText(TestActivity.this,
//                                    "ERROR ", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }
//                });
    }

}
