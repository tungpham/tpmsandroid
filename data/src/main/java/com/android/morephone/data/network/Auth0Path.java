package com.android.morephone.data.network;

import com.android.morephone.data.entity.token.CredentialsEntity;
import com.android.morephone.data.entity.token.TokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by truongnguyen on 9/6/17.
 */

public interface Auth0Path {

    @GET("authorize")
    Call<String> authorize(@Query("audience") String audience,
                           @Query("scope") String scope,
                           @Query("response_type") String responseType,
                           @Query("client_id") String clientId,
                           @Query("redirect_uri") String redirectUri,
                           @Query("code_challenge") String codeChallenge,
                           @Query("code_challenge_method") String codeChallengeMethod);


    @POST("oauth/token")
    Call<CredentialsEntity> token(@Body TokenRequest tokenRequest);

}