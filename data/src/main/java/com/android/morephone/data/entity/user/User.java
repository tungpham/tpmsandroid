package com.android.morephone.data.entity.user;

/**
 * Created by Ethan on 7/26/17.
 */

public class User {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String languageCode;
    private String device;
    private String token;
    private String platform;
    private long createdAt;
    private long updatedAt;

    public User() {
    }

    private User(Builder builder) {
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.country = builder.country;
        this.token = builder.token;
        this.languageCode = builder.languageCode;
        this.device = builder.device;
        this.platform = builder.platform;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void update(String country, String languageCode) {
        this.country = country;
        this.languageCode = languageCode;
    }

    public void update(String token) {
        this.token = token;
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
        private String firstName;
        private String lastName;
        private String country;
        private String languageCode;
        private String token;
        private String device;
        private String platform;

        private Builder() {
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

        public Builder token(String token) {
            this.token = token;
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
