package com.ethan.morephone.jwt.client;

import com.google.common.base.Joiner;

import java.io.UnsupportedEncodingException;

/**
 * Created by Ethan on 8/1/17.
 */

public class IncomingClientScope implements Scope {

    private static final String SCOPE = Joiner.on(':').join("scope", "client", "incoming");

    private final String clientName;

    public IncomingClientScope(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String getPayload() throws UnsupportedEncodingException {
        String query = Joiner.on('=').join("clientName", this.clientName);
        return Joiner.on('?').join(SCOPE, query);
    }
}
