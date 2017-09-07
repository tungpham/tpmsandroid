package com.android.morephone.data.entity.token;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by truongnguyen on 9/7/17.
 */

public class CredentialsEntity {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String type;

    @SerializedName("id_token")
    private String idToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("scope")
    private String scope;

    private Date expiresAt;

    //TODO: Deprecate this constructor
    public CredentialsEntity(@Nullable String idToken, @Nullable String accessToken, @Nullable String type, @Nullable String refreshToken, @Nullable Long expiresIn) {
        this(idToken, accessToken, type, refreshToken, expiresIn, null, null);
    }

    public CredentialsEntity(@Nullable String idToken, @Nullable String accessToken, @Nullable String type, @Nullable String refreshToken, @Nullable Date expiresAt, @Nullable String scope) {
        this(idToken, accessToken, type, refreshToken, null, expiresAt, scope);
    }

    private CredentialsEntity(@Nullable String idToken, @Nullable String accessToken, @Nullable String type, @Nullable String refreshToken, @Nullable Long expiresIn, @Nullable Date expiresAt, @Nullable String scope) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.type = type;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.expiresAt = expiresAt;
        if (expiresAt == null && expiresIn != null) {
            this.expiresAt = new Date(getCurrentTimeInMillis() + expiresIn * 1000);
        }
        if (expiresIn == null && expiresAt != null) {
            this.expiresIn = (expiresAt.getTime() - getCurrentTimeInMillis()) / 1000;
        }
    }

    @VisibleForTesting
    long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Getter for the Identity Token with user information.
     *
     * @return the Identity Token.
     */
    @Nullable
    public String getIdToken() {
        return idToken;
    }

    /**
     * Getter for the Access Token for Auth0 API.
     *
     * @return the Access Token.
     */
    @Nullable
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Getter for the type of the received Token.
     *
     * @return the token type.
     */
    @Nullable
    public String getType() {
        return type;
    }

    /**
     * Getter for the Refresh Token that can be used to request new tokens without signing in again.
     *
     * @return the Refresh Token.
     */
    @Nullable
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Getter for the token lifetime in seconds.
     * Once expired, the token can no longer be used to access an API and a new token needs to be obtained.
     *
     * @return the token lifetime in seconds.
     */
    @Nullable
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Getter for the token's granted scope. Only available if the requested scope differs from the granted one.
     *
     * @return the granted scope.
     */
    @Nullable
    public String getScope() {
        return scope;
    }

    /**
     * Getter for the expiration date of this token.
     * Once expired, the token can no longer be used to access an API and a new token needs to be obtained.
     *
     * @return the expiration date of this token
     */
    @Nullable
    public Date getExpiresAt() {
        return expiresAt;
    }

}
