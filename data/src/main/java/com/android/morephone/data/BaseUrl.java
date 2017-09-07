package com.android.morephone.data;

/**
 * Created by Ethan on 7/25/17.
 */

public class BaseUrl {

//    public static final String BASE_URL = "https://firebase-morephone.appspot.com/api/v1/";
    public static final String BASE_URL = "https://tpmsservice.herokuapp.com/api/v1/";

    public static final String API_IDENTIFIER = "https://tpmsservice.herokuapp.com";
    public static final String SCOPE = "write:phone-number delete:phone-number write:pool-phone-number read:pool-phone-numbers read:phone-numbers read:phone-number write:forward-phone-number write:expire-phone-number write:user write:user-token write:call-token read:records read:call-logs write:send-message read:messages read:usage write:purchase";
    public static final String RESPONSE_TYPE = "code";
    public static final String CLIENT_ID = "JYPws2eLK9Re7Ff5iZlN9T7tQ2sRcN40";
    public static final String REDIRECT_URI = "https://coderdaudat.auth0.com/android/com.morephone.tpms/callback";
    public static final String CODE_CHALLENGE = "CODE_CHALLENGE";
    public static final String CODE_CHALLENGE_METHOD = "S256";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String CODE_VERIFIER = "CODE_VERIFIER";

}
