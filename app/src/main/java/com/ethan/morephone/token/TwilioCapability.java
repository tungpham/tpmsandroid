package com.ethan.morephone.token;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan on 8/1/17.
 */

public class TwilioCapability extends CapabilityToken {
    private String accountSid;
    private String authToken;
    private List<String> scopes;
    private boolean buildIncomingScope = false;
    private String incomingClientName = null;
    private boolean buildOutgoingScope = false;
    private String appSid = null;
    private String outgoingClientName = null;
    private Map<String, String> outgoingParams = null;

    public TwilioCapability(String accountSid, String authToken) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.scopes = new ArrayList();
    }

    private String buildScopeString(String serivce, String priviledge, Map<String, String> params) {
        StringBuilder scope = new StringBuilder();
        scope.append("scope:");
        scope.append(serivce);
        scope.append(":");
        scope.append(priviledge);
        if(params != null && params.size() > 0) {
            String paramsJoined = this.generateParamString(params);
            scope.append("?");
            scope.append(paramsJoined);
        }

        return scope.toString();
    }

    public void allowClientOutgoing(String appSid) {
        this.allowClientOutgoing(appSid, (Map)null);
    }

    public void allowClientOutgoing(String appSid, Map<String, String> params) {
        this.buildOutgoingScope = true;
        this.outgoingParams = params;
        this.appSid = appSid;
    }

    private String generateParamString(Map<String, String> params) {
        ArrayList keyValues = new ArrayList();
        Iterator paramsJoined = params.keySet().iterator();

        while(paramsJoined.hasNext()) {
            String key = (String)paramsJoined.next();
            String value = (String)params.get(key);

            try {
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
                continue;
            }

            keyValues.add(key + "=" + value);
        }

        String paramsJoined1 = StringUtils.join(keyValues, '&');
        return paramsJoined1;
    }

    public void allowClientIncoming(String clientName) {
        this.incomingClientName = clientName;
        this.buildIncomingScope = true;
    }

    public void allowEventStream(Map<String, String> filters) {
        LinkedHashMap value = new LinkedHashMap();
        value.put("path", "/2010-04-01/Events");
        if(filters != null) {
            String paramsJoined = this.generateParamString(filters);
            value.put("params", paramsJoined);
        }

        this.scopes.add(this.buildScopeString("stream", "subscribe", value));
    }

    public String generateToken() throws DomainException {
        return this.generateToken(3600L);
    }

    public String generateToken(long ttl) throws DomainException {
        this.buildIncomingScope();
        this.buildOutgoingScope();

        try {
            LinkedHashMap e = new LinkedHashMap();
            e.put("iss", this.accountSid);
            e.put("exp", String.valueOf((new Date()).getTime() / 1000L + ttl));
            e.put("scope", StringUtils.join(this.scopes, ' '));
            return jwtEncode(e, this.authToken);
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new DomainException(var4);
        }
    }

    private void buildOutgoingScope() {
        if(this.buildOutgoingScope) {
            HashMap values = new HashMap();
            values.put("appSid", this.appSid);
            if(this.outgoingClientName != null) {
                values.put("clientName", this.outgoingClientName);
            } else if(this.incomingClientName != null) {
                values.put("clientName", this.incomingClientName);
            }

            if(this.outgoingParams != null) {
                String paramsJoined = this.generateParamString(this.outgoingParams);
                values.put("appParams", paramsJoined);
            }

            this.scopes.add(this.buildScopeString("client", "outgoing", values));
        }

    }

    private void buildIncomingScope() {
        if(this.buildIncomingScope) {
            LinkedHashMap value = new LinkedHashMap();
            if(this.incomingClientName == null) {
                throw new IllegalStateException("No client name set");
            }

            value.put("clientName", this.incomingClientName);
            this.scopes.add(this.buildScopeString("client", "incoming", value));
        }

    }

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("usage: java com.twilio.client.TwilioCapability accountSid authToken");
        } else {
            TwilioCapability capability = new TwilioCapability(args[0], args[1]);
            capability.allowEventStream((Map)null);
            capability.allowClientIncoming("Frank");
            HashMap params = new HashMap();
            params.put("foo", "fooval");
            capability.allowClientOutgoing("APabe7650f654fc34655fc81ae71caa3ff", params);

            try {
                String e = capability.generateToken();
                System.out.println(e);
            } catch (DomainException var4) {
                var4.printStackTrace();
            }

        }
    }
}
