package com.android.morephone.data.entity.user;

import java.util.Date;
import java.util.List;

/**
 * Created by Ethan on 7/26/17.
 */

public class User {

    private String id;
    private String email;
    private String forwardPhoneNumber;
    private String forwardEmail;
    private String firstName;
    private String lastName;
    private String country;
    private String languageCode;
    private String device;
    private List<TokenFcm> tokenFcms;
    private String platform;
    private String accountSid;
    private String authToken;
    private String applicationSid;
    private long createdAt;
    private long updatedAt;

    public User() {
    }

    private User(Builder builder) {
        this.email = builder.email;
        this.forwardPhoneNumber = builder.forwardPhoneNumber;
        this.forwardEmail = builder.forwardEmail;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.country = builder.country;
        this.tokenFcms = builder.tokenFcms;
        this.languageCode = builder.languageCode;
        this.device = builder.device;
        this.platform = builder.platform;
        this.accountSid = builder.accountSid;
        this.authToken = builder.authToken;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPlatform() {
        return platform;
    }

    public List<TokenFcm> getTokenFcms() {
        return tokenFcms;
    }

    public void setTokenFcms(List<TokenFcm> token) {
        this.tokenFcms = token;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getApplicationSid() {
        return applicationSid;
    }

    public void setApplicationSid(String applicationSid) {
        this.applicationSid = applicationSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getForwardPhoneNumber() {
        return forwardPhoneNumber;
    }

    public void setForwardPhoneNumber(String forwardPhoneNumber) {
        this.forwardPhoneNumber = forwardPhoneNumber;
    }

    public String getForwardEmail() {
        return forwardEmail;
    }

    public void setForwardEmail(String forwardEmail) {
        this.forwardEmail = forwardEmail;
    }

    public void update(String country, String languageCode) {
        this.country = country;
        this.languageCode = languageCode;
    }

    public void update(List<TokenFcm> token) {
        this.tokenFcms = token;
        Date date = new Date();
        updatedAt = date.getTime();
    }

    public void updateForward(String forwardPhoneNumber, String forwardEmail) {
        this.forwardPhoneNumber = forwardPhoneNumber;
        this.forwardEmail = forwardEmail;
        Date date = new Date();
        updatedAt = date.getTime();
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, email=%s, Country=%s]",
                this.id,
                this.email,
                this.country
        );
    }


    public static class Builder {
        private String email;
        private String forwardPhoneNumber;
        private String forwardEmail;
        private String firstName;
        private String lastName;
        private String country;
        private String languageCode;
        private List<TokenFcm> tokenFcms;
        private String device;
        private String accountSid;
        private String authToken;
        private String platform;

        private Builder() {
        }


        public Builder forwardPhoneNumber(String forwardPhoneNumber) {
            this.forwardPhoneNumber = forwardPhoneNumber;
            return this;
        }

        public Builder forwardEmail(String forwardEmail) {
            this.forwardEmail = forwardEmail;
            return this;
        }

        public Builder accountSid(String accountSid) {
            this.accountSid = accountSid;
            return this;
        }

        public Builder authToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder languageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder token(List<TokenFcm> tokenFcms) {
            this.tokenFcms = tokenFcms;
            return this;
        }


        public Builder device(String device) {
            this.device = device;
            return this;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public User build() {

            User build = new User(this);

            build.checkEmail(build.getEmail());

            return build;
        }
    }

    private void checkEmail(String email) {
    }


}
