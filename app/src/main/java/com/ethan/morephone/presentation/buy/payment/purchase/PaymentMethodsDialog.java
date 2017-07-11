package com.ethan.morephone.presentation.buy.payment.purchase;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 12/11/16.
 */

public class PaymentMethodsDialog extends DialogFragment {

    public static PaymentMethodsDialog getInstance() {
        return new PaymentMethodsDialog();
    }

    public static final int PAYMENT_VIA_CREDIT = 0;
    public static final int PAYMENT_VIA_PAYPAL = PAYMENT_VIA_CREDIT + 1;


    private PaymentMethodsListener mPaymentMethodsListener;

    @Override
    public void show(FragmentManager manager, String tag) {

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AppCompatDialog dialog = new AppCompatDialog(getContext());
        dialog.setContentView(R.layout.dialog_payment_methods);
        dialog.findViewById(R.id.text_payment_paypal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPaymentMethodsListener != null){
                    mPaymentMethodsListener.onChoosePaymentMethods(PAYMENT_VIA_PAYPAL);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.text_payment_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPaymentMethodsListener != null){
                    mPaymentMethodsListener.onChoosePaymentMethods(PAYMENT_VIA_CREDIT);
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public void setPaymentMethodsListener(PaymentMethodsListener paymentMethodsListener) {
        mPaymentMethodsListener = paymentMethodsListener;
    }

    public interface PaymentMethodsListener {
        void onChoosePaymentMethods(int paymentMethods);
    }

}
