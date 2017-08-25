package com.ethan.morephone.presentation.buy.payment.stripe;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;


/**
 * Created by Ethan on 5/26/17.
 */

public class PaymentActivity extends FragmentActivity {
    //keep track of your current environment,
    //change to WalletConstants.ENVIRONMENT_PRODUCTION when you're ready to go live
//    public static final int mEnvironment = WalletConstants.ENVIRONMENT_TEST;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == LOAD_MASKED_WALLET_REQUEST_CODE) {
//
//        } else if (requestCode == LOAD_FULL_WALLET_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
//                String tokenJSON = fullWallet.getPaymentMethodToken().getToken();
//
//                //A token will only be returned in production mode,
//                //i.e. WalletConstants.ENVIRONMENT_PRODUCTION
//                if (mEnvironment == WalletConstants.ENVIRONMENT_PRODUCTION) {
//                    try {
//                        Token token = TokenParser.parseToken(tokenJSON);
//                        // TODO: send token to your server
//                    } catch (JSONException jsonException) {
//                        // Log the error and notify Stripe helpß
//                    }
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void createSampleCartAndLaunchAndroidPayActivity() {
//        AndroidPayConfiguration.init(FUNCTIONAL_SOURCE_PUBLISHABLE_KEY, "USD");
//        AndroidPayConfiguration androidPayConfiguration = AndroidPayConfiguration.getInstance();
//        androidPayConfiguration.setShippingAddressRequired(true);
//        CartManager cartManager = new CartManager("USD");
//        cartManager.addLineItem("Llama Food", 5000L);
//        cartManager.addLineItem("Llama Shoes", 4, 2000L);
//        cartManager.addShippingLineItem("Domestic shipping estimate", 1000L);
//
//        try {
//            Cart cart = cartManager.buildCart();
//            Intent intent = new Intent(this, AndroidPayActivity.class)
//                    .putExtra(StripeAndroidPayActivity.EXTRA_CART, cart);
//            startActivity(intent);
//        } catch (CartContentException unexpected) {
//            // Ignore for now.
//        }
    }
}
