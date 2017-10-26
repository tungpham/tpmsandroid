package com.ethan.morephone.token;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ethan on 8/1/17.
 */

public class CapabilityToken {
    public CapabilityToken() {
    }

    protected static String jwtEncode(Map<String, Object> payload, String key) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        LinkedHashMap header = new LinkedHashMap();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        ArrayList segments = new ArrayList();
        segments.add(encodeBase64(jsonEncode(header)));
        segments.add(encodeBase64(jsonEncode(payload)));
        String signingInput = StringUtils.join(segments, ".");
        String signature = sign(signingInput, key);
        segments.add(signature);
        return StringUtils.join(segments, ".");
    }

    private static String jsonEncode(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json.replace("\\/", "/");
    }

    private static String encodeBase64(String data) throws UnsupportedEncodingException {
        return encodeBase64(data.getBytes("UTF-8"));
    }

    private static String encodeBase64(byte[] data) throws UnsupportedEncodingException {
        String encodedString = new String(Base64.encodeBase64(data));
        return encodedString.replace('+', '-').replace('/', '_').replace("=", "");
    }

    private static String sign(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
        return encodeBase64(rawHmac);
    }

    public static class DomainException extends Exception {
        public DomainException(String message) {
            super(message);
        }

        public DomainException(Exception e) {
            super(e);
        }
    }
}
