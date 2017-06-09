package com.ethan.morephone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 6/9/17.
 */

public class UserFacebookModel {

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("id")
    public String id;

    public String gender;

    public String email;

    public String birthday;

    public String location;

    public UserFacebookModel(String lastName, String firstName, String id, String gender, String email, String birthday, String location) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.id = id;
        this.gender = gender;
        this.email = email;
        this.birthday = birthday;
        this.location = location;
    }
}
