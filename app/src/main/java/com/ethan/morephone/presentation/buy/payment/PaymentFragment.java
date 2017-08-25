package com.ethan.morephone.presentation.buy.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by Ethan on 5/3/17.
 */

public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    public static PaymentFragment getInstance() {
        return new PaymentFragment();
    }

    private static final int REQUEST_CODE_PAYMENT = 1;

//    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
//    private static final String CONFIG_CLIENT_ID = "AeyY3-oFAor-ELi0jPGQXWAAvWlj-0FlS1gn9mJ4BCpADKgFeqzGucmJwJGeUOcTT5T8lRQObZPdQQGm";
//
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(CONFIG_ENVIRONMENT)
//            .clientId(CONFIG_CLIENT_ID)
//            // The following are only used in PayPalFuturePaymentActivity.
//            .merchantName("More Phone Merchant")
//            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(getActivity(), PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        getActivity().startService(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        view.findViewById(R.id.text_payment_add).setOnClickListener(this);
        view.findViewById(R.id.text_payment_visa).setOnClickListener(this);
        view.findViewById(R.id.text_payment_paypal).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_payment_add:
//                startActivity(new Intent(getActivity(), CardActivity.class));
                break;
            case R.id.text_payment_paypal:
//                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//                Intent intent = new Intent(getActivity(), com.paypal.android.sdk.payments.PaymentActivity.class);
//
//                // send the same configuration for restart resiliency
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//                intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                break;
            default:
                break;
        }
    }

//    private PayPalPayment getThingToBuy(String paymentIntent) {
//        return new PayPalPayment(new BigDecimal("0.01"), "USD", "Add fund More Phone", paymentIntent);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        DebugTool.logD("" + confirm.toJSONObject().toString(4));
//                        DebugTool.logD("" + confirm.getPayment().toJSONObject().toString(4));
//                        /**
//                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
//                         * or consent completion.
//                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
//                         * for more details.
//                         *
//                         * For sample mobile backend interactions, see
//                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
//                         */
//                        Toast.makeText(
//                                getContext(),
//                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
//                                .show();
//
//                    } catch (JSONException e) {
//                        DebugTool.logD("an extremely unlikely failure occurred: ");
//                    }
                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                DebugTool.logD("The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                DebugTool.logD("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
    }
}
