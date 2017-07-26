package com.android.morephone.data.entity;

/**
 * Created by Ethan on 7/26/17.
 */

public class BaseResponse<T> {

    private final T response;
    private final int status;

    public BaseResponse(T response, int status) {
        this.response = response;
        this.status = status;
    }

    public T getResponse() { return response; }
    public int getStatus() { return status; }
}
