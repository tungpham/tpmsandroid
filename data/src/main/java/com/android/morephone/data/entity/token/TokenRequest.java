package com.android.morephone.data.entity.token;

import com.google.gson.annotations.SerializedName;

/**
 * Created by truongnguyen on 9/7/17.
 */

public class TokenRequest {

    @SerializedName("grant_type")
    public String grantType;

    @SerializedName("client_id")
    public String clientId;

    @SerializedName("code_verifier")
    public String codeVerifier;

    @SerializedName("code")
    public String code;

    @SerializedName("redirect_uri")
    public String redirectUri;

    public TokenRequest(String grantType, String clientId, String codeVerifier, String code, String redirectUri) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.codeVerifier = codeVerifier;
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
