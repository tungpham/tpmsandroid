package com.ethan.morephone.jwt.client;

import java.io.UnsupportedEncodingException;

/**
 * Created by Ethan on 8/1/17.
 */

public interface Scope {

    public String getPayload() throws UnsupportedEncodingException;
}
