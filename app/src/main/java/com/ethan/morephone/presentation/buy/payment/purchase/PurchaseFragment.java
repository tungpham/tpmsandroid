package com.ethan.morephone.presentation.buy.payment.purchase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by Ethan on 5/4/17.
 */

public class PurchaseFragment extends BaseFragment implements
        PaymentMethodsDialog.PaymentMethodsListener,
        View.OnClickListener {

    public static PurchaseFragment getInstance() {
        return new PurchaseFragment();
    }


    private static final int REQUEST_CODE_PAYMENT = 1;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AUOoaumERgukf8fbz6pPyH3e0jqyOBzEjvgdsSagrQB1oVwzdfLqgfgFHEMNZquHUY-gEfrGbtozDUFW";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        view.findViewById(R.id.button_purchase_pay_now).setOnClickListener(this);
        return view;
    }

    @Override
    public void onChoosePaymentMethods(int paymentMethods) {
        if(paymentMethods == PaymentMethodsDialog.PAYMENT_VIA_PAYPAL){
            PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_ORDER);

            Intent intent = new Intent(getActivity(), PaymentActivity.class);

            // send the same configuration for restart resiliency
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_purchase_pay_now:
                PaymentMethodsDialog paymentMethodsDialog = PaymentMethodsDialog.getInstance();
                paymentMethodsDialog.show(getChildFragmentManager(), PaymentMethodsDialog.class.getSimpleName());
                paymentMethodsDialog.setPaymentMethodsListener(this);
                break;
            default:
                break;
        }
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("0.01"), "USD", "Buy a phone",
                paymentIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        DebugTool.logD("" + confirm.toJSONObject().toString(4));
                        DebugTool.logD("" + confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        DebugTool.logD("an extremely unlikely failure occurred: ");
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                DebugTool.logD("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                DebugTool.logD("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}
