package com.android.morephone.data.entity.purchase;

/**
 * Created by Ethan on 7/20/17.
 */

public class MorePhonePurchase {

    public String id;
    public String userId;
    public String packageName;
    public String token;
    public int purchaseState;
    public String orderId;
    public long purchaseTime;
    public String productId;
    public long createdAt;
    public long updatedAt;

    public MorePhonePurchase(String userId, String packageName, String token, int purchaseState, String orderId, long purchaseTime, String productId) {
        this.userId = userId;
        this.packageName = packageName;
        this.token = token;
        this.purchaseState = purchaseState;
        this.orderId = orderId;
        this.purchaseTime = purchaseTime;
        this.productId = productId;
    }
}
