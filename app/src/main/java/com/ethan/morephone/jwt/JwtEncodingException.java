package com.ethan.morephone.jwt;

/**
 * Created by Ethan on 8/1/17.
 */

public class JwtEncodingException extends RuntimeException {
    public JwtEncodingException(Exception e) {
        super(e);
    }
}
