package com.ethan.morephone.presentation.buy.payment.fund;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyApplication;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.checkout.ActivityCheckout;
import com.ethan.morephone.presentation.buy.payment.checkout.Billing;
import com.ethan.morephone.presentation.buy.payment.checkout.BillingRequests;
import com.ethan.morephone.presentation.buy.payment.checkout.Checkout;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.PurchaseFlow;
import com.ethan.morephone.presentation.buy.payment.checkout.RequestListener;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.AvailableSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.PurchasedSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.TargetSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;
import com.ethan.morephone.presentation.buy.payment.purchase.PaymentMethodsDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.SUBSCRIPTION;

/**
 * Created by Ethan on 5/10/17.
 */

public class AddFundFragment extends BaseFragment implements
        View.OnClickListener,
        PaymentMethodsDialog.PaymentMethodsListener {

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_PAYMENT_VISA = 2;

//    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
//    private static final String CONFIG_CLIENT_ID = "AUOoaumERgukf8fbz6pPyH3e0jqyOBzEjvgdsSagrQB1oVwzdfLqgfgFHEMNZquHUY-gEfrGbtozDUFW";
//
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(CONFIG_ENVIRONMENT)
//            .clientId(CONFIG_CLIENT_ID)
//            // The following are only used in PayPalFuturePaymentActivity.
//            .merchantName("Example Merchant")
//            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    private static final List<String> SKUS = Arrays.asList("sub_01");
    private ActivityCheckout mCheckout;
    private RecyclerView mPurchasedSkus;
    private Spinner mTargetSkus;
    private Spinner mAvailableSkus;
    private final List<Inventory.Callback> mInventoryCallbacks = new ArrayList<>();

    private double mBalanceAdd = 0;

    public static AddFundFragment getInstance() {
        return new AddFundFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(getActivity(), PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        getActivity().startService(intent);
    }

    private PurchasedSkusAdapter mPurchasedSkusAdapter;
    private TargetSkusAdapter mTargetSkusAdapter;
//    private AppCompatButton mButtonAddFund;
    private Button mBuy;
    private Button mChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_subscriptions, container, false);
//        view.findViewById(R.id.button_purchase_add_fund).setOnClickListener(this);
        mBuy = (Button) view.findViewById(R.id.buy);
        mChange = (Button) view.findViewById(R.id.change);
        mPurchasedSkus = (RecyclerView) view.findViewById(R.id.purchased_skus);
        mTargetSkus = (Spinner) view.findViewById(R.id.target_skus);
        mAvailableSkus = (Spinner) view.findViewById(R.id.available_skus);

        initAvailableSkus();
        initPurchasedSkus();
        initTargetSkus();

        final Billing billing = MyApplication.get(getActivity()).getBilling();
        mCheckout = Checkout.forActivity(getActivity(), billing);
        mCheckout.start();
        reloadInventory();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_purchase_add_fund:
//                PaymentMethodsDialog paymentMethodsDialog = PaymentMethodsDialog.getInstance();
//                paymentMethodsDialog.show(getChildFragmentManager(), PaymentMethodsDialog.class.getSimpleName());
//                paymentMethodsDialog.setPaymentMethodsListener(this);
                onChoosePaymentMethods(PaymentMethodsDialog.PAYMENT_VIA_PAYPAL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onChoosePaymentMethods(int paymentMethods) {
        if (paymentMethods == PaymentMethodsDialog.PAYMENT_VIA_PAYPAL) {
//            PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//            Intent intent = new Intent(getActivity(), com.paypal.android.sdk.payments.PaymentActivity.class);
//
//            // send the same configuration for restart resiliency
//            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//            intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//            startActivityForResult(intent, REQUEST_CODE_PAYMENT);

        } else if (paymentMethods == PaymentMethodsDialog.PAYMENT_VIA_CREDIT) {
//            startActivityForResult(new Intent(getActivity(), CardActivity.class), REQUEST_CODE_PAYMENT_VISA);
        }
    }

//    private PayPalPayment getThingToBuy(String paymentIntent) {
//        return new PayPalPayment(new BigDecimal("0.01"), "USD", "Add Fund", paymentIntent);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCheckout.onActivityResult(requestCode, resultCode, data);
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
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                DebugTool.logD("The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                DebugTool.logD("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
        } else if (requestCode == REQUEST_CODE_PAYMENT_VISA) {
//            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
//            String cardCvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
//            String cardExpiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
//            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);

//            DebugTool.logD("CardNumber: " + cardNumber);
//            DebugTool.logD("cardHolderName: " + cardHolderName);
//            DebugTool.logD("cardExpiry: " + cardExpiry);
//            DebugTool.logD("cardCvv: " + cardCvv);
        }
    }

    @Override
    public void onDestroy() {
//        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        mPurchasedSkusAdapter.saveSate(out);
        super.onSaveInstanceState(out);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPurchasedSkusAdapter.restoreState(savedInstanceState);
    }

    private void initPurchasedSkus() {
        mPurchasedSkusAdapter = new PurchasedSkusAdapter(getActivity(), new PurchasedSkusAdapter.Listener() {
            @Override
            public void onCheckedChanged() {
                updateTargetSkusVisibility();
            }
        });
        mPurchasedSkus.setAdapter(mPurchasedSkusAdapter);
        mPurchasedSkus.setLayoutManager(new LinearLayoutManager(getContext()));
        mInventoryCallbacks.add(mPurchasedSkusAdapter);
    }

    private void initAvailableSkus() {
        final AvailableSkusAdapter adapter = new AvailableSkusAdapter(getContext());
        mAvailableSkus.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                mBuy.setEnabled(adapter.getCount() > 0);
            }
        });
        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = mAvailableSkus.getSelectedItemPosition();
                if (position >= 0 && position < adapter.getCount()) {
                    final SkuItem item = adapter.getItem(position);
                    if (item != null) {
                        purchase(item.getSku());
                    }
                }
            }
        });
        mInventoryCallbacks.add(adapter);
    }

    private void reloadInventory() {
        final Inventory.Request request = Inventory.Request.create();
        request.loadPurchases(SUBSCRIPTION);
        request.loadSkus(SUBSCRIPTION, SKUS);
        mCheckout.loadInventory(request, new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                for (Inventory.Callback callback : mInventoryCallbacks) {
                    DebugTool.logD("CALLBACK: " + products.size());
                    callback.onLoaded(products);
                }
            }


        });
    }

    private void initTargetSkus() {
        mTargetSkusAdapter = new TargetSkusAdapter(getContext());
        mTargetSkus.setAdapter(mTargetSkusAdapter);
        mTargetSkusAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateTargetSkusVisibility();
            }
        });
        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Set<Sku> checked = mPurchasedSkusAdapter.getChecked();
                if (checked.isEmpty()) {
                    return;
                }
                final int position = mTargetSkus.getSelectedItemPosition();
                if (position >= 0 && position < mTargetSkusAdapter.getCount()) {
                    final SkuItem item = mTargetSkusAdapter.getItem(position);
                    if (item != null) {
                        change(checked, item.getSku());
                    }
                }
            }
        });
        mInventoryCallbacks.add(mTargetSkusAdapter);
    }

    public double getBalance(){
        return mBalanceAdd;
    }

    private void updateTargetSkusVisibility() {
        final boolean enabled = mTargetSkusAdapter.getCount() > 0 && mPurchasedSkusAdapter.getChecked().size() > 0;
//        mChange.setEnabled(enabled);
        mTargetSkus.setEnabled(enabled);
    }

    private void purchase(Sku sku) {
        mCheckout.startPurchaseFlow(sku, null, new PurchaseListener());
    }

    private void change(final Set<Sku> old, final Sku sku) {
        mCheckout.whenReady(new Checkout.EmptyListener() {
            @Override
            public void onReady(@Nonnull BillingRequests requests) {
                final PurchaseFlow flow = mCheckout.createOneShotPurchaseFlow(new PurchaseListener());
                requests.changeSubscription(new ArrayList<>(old), sku, null, flow);
            }
        });
    }

    private class PurchaseListener implements RequestListener<Purchase> {
        @Override
        public void onSuccess(@Nonnull Purchase result) {
            DebugTool.logD("SKU: " + result.sku);
            DebugTool.logD("Purchase: " + result.toJson());
            reloadInventory();
        }

        @Override
        public void onError(int response, @Nonnull Exception e) {
            reloadInventory();
        }
    }
}
