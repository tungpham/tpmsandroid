package com.android.morephone.data.entity.usage;

/**
 * Created by Ethan on 7/29/17.
 */

public class UsageItem {

    private String id;
    private String userId;
    private String accountSid;
    private double balance;
    private long messageIncoming;
    private long messageOutgoing;
    private long callIncoming;
    private long callOutgoing;
    private long createdAt;
    private long updatedAt;

    public UsageItem() {

    }

    public UsageItem(String userId, String accountSid, double balance, long messageIncoming, long messageOutgoing, long callIncoming, long callOutgoing) {
        this.userId = userId;
        this.accountSid = accountSid;
        this.balance = balance;
        this.messageIncoming = messageIncoming;
        this.messageOutgoing = messageOutgoing;
        this.callIncoming = callIncoming;
        this.callOutgoing = callOutgoing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getMessageIncoming() {
        return messageIncoming;
    }

    public void setMessageIncoming(long messageIncoming) {
        this.messageIncoming = messageIncoming;
    }

    public long getMessageOutgoing() {
        return messageOutgoing;
    }

    public void setMessageOutgoing(long messageOutgoing) {
        this.messageOutgoing = messageOutgoing;
    }

    public long getCallIncoming() {
        return callIncoming;
    }

    public void setCallIncoming(long callIncoming) {
        this.callIncoming = callIncoming;
    }

    public long getCallOutgoing() {
        return callOutgoing;
    }

    public void setCallOutgoing(long callOutgoing) {
        this.callOutgoing = callOutgoing;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
