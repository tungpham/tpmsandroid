package com.android.morephone.data.entity.user;

/**
 * Created by truongnguyen on 11/29/17.
 */

public class TokenFcm {

    private int platform;
    private String token;

    public TokenFcm(int platform, String token) {
        this.platform = platform;
        this.token = token;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
