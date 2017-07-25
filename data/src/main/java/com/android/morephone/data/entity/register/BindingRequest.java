package com.android.morephone.data.entity.register;

/**
 * Created by Ethan on 7/25/17.
 */

public class BindingRequest {

    public String identity;
    public String endpoint;
    public String address;
    public String binding;

    public BindingRequest(String identity, String endpoint, String address, String binding) {
        this.identity = identity;
        this.endpoint = endpoint;
        this.address = address;
        this.binding = binding;
    }
}
