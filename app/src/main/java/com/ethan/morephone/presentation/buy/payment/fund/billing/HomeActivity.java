//package com.ethan.morephone.presentation.buy.payment.fund.billing;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.Purchase;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Created by truongnguyen on 8/29/17.
// */
//
//public class HomeActivity {
//    @SuppressLint("StaticFieldLeak")
//    private static BillingManager mBillingManager;
//    private InAPPBillingSetup mViewController;
//    private Activity activity;
//
//    /**
//     * Create and initialize BillingManager which talks to BillingLibrary
//     * @param activity pass Activity
//     * @param SKU_ID this product list id from google play
//     * @param BASE_64_ENCODED Base64-encoded RSA public key.
//     */
//    public HomeActivity(Activity activity, String SKU_ID, String BASE_64_ENCODED) {
//        this.activity = activity;
//        mViewController = new InAPPBillingSetup(this, SKU_ID);
//
//        if (mBillingManager == null)
//            mBillingManager = new BillingManager(activity, mViewController.getUpdateListener(), BASE_64_ENCODED);
//        mBillingManager.initiatePurchaseFlow(SKU_ID, BillingClient.SkuType.INAPP);
//    }
//
//    /**
//     * After google payment successfully get response
//     * @param purchaseList successful payment receive response
//     */
//
//    void onPurchasesUpdated(List<Purchase> purchaseList) {
//        Intent resultIntent = new Intent();
//        Bundle args = new Bundle();
//        args.putSerializable("result", (Serializable) purchaseList);
//        resultIntent.putExtra("PurchaseBundle", args);
//        activity.setResult(Activity.RESULT_OK, resultIntent);
//        activity.finish();
//    }
//
//}