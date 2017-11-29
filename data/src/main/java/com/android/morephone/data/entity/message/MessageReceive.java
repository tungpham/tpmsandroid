package com.android.morephone.data.entity.message;

/**
 * Created by truongnguyen on 11/29/17.
 */

public class MessageReceive {
    private String smsMessageSid;
    private String smsSid;
    private String smsStatus;
    private String messageSid;
    private String accountSid;
    private String body;
    private String from;
    private String to;

    public MessageReceive(String smsMessageSid, String smsSid, String smsStatus, String messageSid, String accountSid, String body, String from, String to) {
        this.smsMessageSid = smsMessageSid;
        this.smsSid = smsSid;
        this.smsStatus = smsStatus;
        this.messageSid = messageSid;
        this.accountSid = accountSid;
        this.body = body;
        this.from = from;
        this.to = to;
    }

    public String getSmsMessageSid() {
        return smsMessageSid;
    }

    public void setSmsMessageSid(String smsMessageSid) {
        this.smsMessageSid = smsMessageSid;
    }

    public String getSmsSid() {
        return smsSid;
    }

    public void setSmsSid(String smsSid) {
        this.smsSid = smsSid;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getMessageSid() {
        return messageSid;
    }

    public void setMessageSid(String messageSid) {
        this.messageSid = messageSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
