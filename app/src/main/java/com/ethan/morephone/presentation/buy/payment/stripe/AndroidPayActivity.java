package com.ethan.morephone.presentation.buy.payment.stripe;

import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Currency;
import java.util.Locale;

/**
 * Created by Ethan on 5/26/17.
 */

public class AndroidPayActivity {


    private static final Locale LOC = Locale.US;
    private static final Currency USD = Currency.getInstance("USD");

    private ViewGroup mChangeDetailsContainer;
    private ViewGroup mConfirmDetailsContainer;
    private ViewGroup mFragmentContainer;
//    private ListViewController mListViewController;
//    private ProgressDialogController mProgressDialogController;

//    private MaskedWallet mPossibleConfirmedMaskedWallet;

    private TextView mItemsPriceDisplay;
    private TextView mShippingDisplay;
    private TextView mTaxesDisplay;
    private TextView mTotalPaymentDisplay;
    private TextView mSelectedCardDisplay;
    private TextView mSelectedShippingAddressDisplay;
}
