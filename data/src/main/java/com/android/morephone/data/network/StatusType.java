package com.android.morephone.data.network;

/**
 * Created by Ethan on 7/29/17.
 */

public interface StatusType {
    /**
     * Get the associated status code
     *
     * @return the status code
     */
    public int getStatusCode();

    /**
     * Get the class of status code
     *
     * @return the class of status code
     */
    public HTTPStatus.Family getFamily();

    /**
     * Get the reason phrase
     *
     * @return the reason phrase
     */
    public String getReasonPhrase();
}
