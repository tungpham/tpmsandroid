package com.ethan.morephone.presentation.buy.payment.fund;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.ethan.morephone.MyApplication;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.checkout.Billing;
import com.ethan.morephone.presentation.buy.payment.checkout.BillingRequests;
import com.ethan.morephone.presentation.buy.payment.checkout.Checkout;
import com.ethan.morephone.presentation.buy.payment.checkout.EmptyRequestListener;
import com.ethan.morephone.presentation.buy.payment.checkout.IntentStarter;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.UiCheckout;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by Ethan on 5/10/17.
 */

public class AddFundFragment extends BaseFragment implements
        View.OnClickListener {

    private static final String AD_FREE = "add_fund";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_PAYMENT_VISA = 2;

    private UiCheckout mCheckout;
    private final List<Inventory.Callback> mInventoryCallbacks = new ArrayList<>();

    private double mBalanceAdd = 0;


    public static AddFundFragment getInstance() {
        return new AddFundFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Activity activity = (Activity) context;
        final Billing billing = MyApplication.get(activity).getBilling();
        mCheckout = Checkout.forUi(new MyIntentStarter(this), this, billing);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckout.start();
    }


    private Purchase mPurchase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_subscriptions, container, false);
        view.findViewById(R.id.buy).setOnClickListener(this);
        RecyclerView mPurchasedSkus = (RecyclerView) view.findViewById(R.id.purchased_skus);
        Spinner mTargetSkus = (Spinner) view.findViewById(R.id.target_skus);
        Spinner mAvailableSkus = (Spinner) view.findViewById(R.id.available_skus);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCheckout.loadInventory(Inventory.Request.create().loadAllPurchases(), new InventoryCallback());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCheckout.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        mCheckout.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                final Purchase purchase = mPurchase;
                if (purchase == null) {
                    mCheckout.startPurchaseFlow(ProductTypes.IN_APP, AD_FREE, null, new PurchaseListener());
                } else {
                    mCheckout.whenReady(new Checkout.EmptyListener() {
                        @Override
                        public void onReady(@Nonnull BillingRequests requests) {
                            requests.consume(purchase.token, new ConsumeListener());
                        }
                    });
                }
                break;
        }
    }

    private void onPurchaseChanged() {
//        mBuyConsume.setText(mPurchase != null ? R.string.consume : R.string.buy);
    }

    private class ConsumeListener extends EmptyRequestListener<Object> {
        @Override
        public void onSuccess(@Nonnull Object result) {
            mPurchase = null;
            onPurchaseChanged();
        }
    }

    private class PurchaseListener extends EmptyRequestListener<Purchase> {
        @Override
        public void onSuccess(@Nonnull Purchase purchase) {
            mPurchase = purchase;
            onPurchaseChanged();
        }
    }

    public double getBalance() {
        return mBalanceAdd;
    }

    private class InventoryCallback implements Inventory.Callback {
        @Override
        public void onLoaded(@Nonnull Inventory.Products products) {
            final Inventory.Product product = products.get(ProductTypes.IN_APP);
            if (!product.supported) {
                return;
            }
            mPurchase = product.getPurchaseInState(AD_FREE, Purchase.State.PURCHASED);
            onPurchaseChanged();
//            mBuyConsume.setEnabled(true);
        }
    }

    /**
     * Trivial implementation of {@link IntentStarter} for the support lib's {@link Fragment}.
     */
    private static class MyIntentStarter implements IntentStarter {
        @Nonnull
        private final Fragment mFragment;

        public MyIntentStarter(@Nonnull Fragment fragment) {
            mFragment = fragment;
        }

        @Override
        public void startForResult(@Nonnull IntentSender intentSender, int requestCode, @Nonnull Intent intent) throws IntentSender.SendIntentException {
            mFragment.startIntentSenderForResult(intentSender, requestCode, intent, 0, 0, 0, null);
        }
    }
}
