package com.ethan.morephone.presentation.buy.payment.fund;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.purchase.MorePhonePurchase;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.checkout.ActivityCheckout;
import com.ethan.morephone.presentation.buy.payment.checkout.EmptyRequestListener;
import com.ethan.morephone.presentation.buy.payment.checkout.IntentStarter;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.checkout.UiCheckout;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.AddFundAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.IN_APP;

/**
 * Created by Ethan on 5/10/17.
 */

public class AddFundFrag extends BaseFragment implements
        View.OnClickListener,
        AddFundAdapter.OnItemPurchaseClickListener,
        UiCheckout.DestroyCheckoutListener,
        BillingProcessor.IBillingHandler {

    private BillingProcessor mBillingProcessor;
    private boolean readyToPurchase = false;

    public static AddFundFrag getInstance() {
        return new AddFundFrag();
    }

//    private static final String SKUS = "add_fund";

    private static final List<String> SKUS = Arrays.asList("add_fund");


    private ActivityCheckout mCheckout;

    //    private UiCheckout mUiCheckout;
    @Nullable
    private Purchase mPurchase;

    private AddFundAdapter mAddFundAdapter;

    private final List<Inventory.Callback> mInventoryCallbacks = new ArrayList<>();

    private double mBalanceAdd = 0;

    private boolean isApprovalBuy = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final Activity activity = (Activity) context;
//        final Billing billing = MyApplication.get(getActivity()).getBilling();
//        mUiCheckout = Checkout.forUi(new MyIntentStarter(this), this, billing);
//        mUiCheckout.start();

        if (!BillingProcessor.isIabServiceAvailable(getActivity())) {
            Toast.makeText(getContext(), getString(R.string.all_in_app_billing_error), Toast.LENGTH_SHORT).show();
//            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        } else {
            mBillingProcessor = new BillingProcessor(getActivity(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhziPq0YT4evePmkFel/p8/paiABLGTbCCRqc9rSU1OypyOqpSD851o6VeeC7hDJyyYPbQnGtB/7sHYP+brFNr2Nqrlmb90aVh0FO/40DQO0/aMuhgyECpLJ7IJXZ9MLuqOscZ2qnatVBbpTHy7sJb/a8ZCXOXlMHpPlcYI7kaCJ+0nm2t16ltDF0CxMz5FUoUyb6KfqFkHMe/48HcGfQVoxP3JOpDe5tP3KITAXDsOEXCLWpYVNFtvM0wImOYmAc6BqRHvNL2WTIex87DWtnQfxt3qrRfn0jv/rDv8t5hguutsToHC9pr/DWEY4wPkkGFdU308tMe+l9HkWpxK/jqQIDAQAB", this);
            mBillingProcessor.initialize();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fund, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        view.findViewById(R.id.button_purchase_add_fund).setOnClickListener(this);

        mAddFundAdapter = new AddFundAdapter(getContext(), new ArrayList<SkuItem>(), this);
        recyclerView.setAdapter(mAddFundAdapter);

//        mInventoryCallbacks.add(mAddFundAdapter);
//
//        final Billing billing = MyApplication.get(getActivity()).getBilling();
//        mCheckout = Checkout.forActivity(getActivity(), billing);
//        mCheckout.start();
//        reloadInventory();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_purchase_add_fund:
//                final Purchase purchase = mPurchase;
//                if (purchase == null) {
//                    mUiCheckout.startPurchaseFlow(ProductTypes.IN_APP, SKUS, null, new PurchaseListener());
//                } else {
//                    DebugTool.logD("PURCHASE OK");
//                    mUiCheckout.whenReady(new Checkout.EmptyListener() {
//                        @Override
//                        public void onReady(@Nonnull BillingRequests requests) {
//                            requests.consume(purchase.token, new ConsumeListener());
//                        }
//                    });
//                }
                DebugTool.logD("READY: " + readyToPurchase);
                if (readyToPurchase)
                    mBillingProcessor.consumePurchase("add_fund");
                break;
            default:
                break;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mUiCheckout.loadInventory(Inventory.Request.create().loadAllPurchases(), new InventoryCallback());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mUiCheckout.onActivityResult(requestCode, resultCode, data);
        if (!mBillingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
//        mUiCheckout.stop();
        if (mBillingProcessor != null)
            mBillingProcessor.release();
        super.onDestroy();
    }


    @Override
    public void onItemClick(SkuItem skuItem) {
        if (isApprovalBuy) {
//            mUiCheckout.loadInventory(Inventory.Request.create().loadAllPurchases(), new InventoryCallback(skuItem.getSku()));
//            mUiCheckout.setDestroyCheckoutListener(this);
            isApprovalBuy = !isApprovalBuy;
        }
    }

    public double getBalance() {
        return mBalanceAdd;
    }

    private void reloadInventory() {
        final Inventory.Request request = Inventory.Request.create();
        request.loadPurchases(IN_APP);
        request.loadSkus(IN_APP, SKUS);
        mCheckout.loadInventory(request, new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                DebugTool.logD("PRODUCTS: " + products.size());
                for (Inventory.Callback callback : mInventoryCallbacks) {
                    callback.onLoaded(products);
                }
            }

        });
    }

    @Override
    public void onDestroyCheckout() {
        isApprovalBuy = !isApprovalBuy;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        DebugTool.logD("PRODUCCT ID: " + productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        DebugTool.logD("HISTORY");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        DebugTool.logD("ERROR");
    }

    @Override
    public void onBillingInitialized() {
        DebugTool.logD("Initialized");
        readyToPurchase = true;
        for (String s : mBillingProcessor.listOwnedProducts()) {
            DebugTool.logD("BILIING: " + s);
        }

        for (String s : mBillingProcessor.listOwnedSubscriptions()) {
            DebugTool.logD("BILIING DES: " + s);
        }
        boolean isPurchase = mBillingProcessor.isPurchased("add_fund");
        if (isPurchase) {
            DebugTool.logD("PURCHAED");
        } else {
            DebugTool.logD("PURCHAED FAIL");
        }


    }


    private class ConsumeListener extends EmptyRequestListener<Object> {

        private Sku mSku;

        public ConsumeListener(Sku sku) {
            mSku = sku;
        }

        @Override
        public void onSuccess(@Nonnull Object result) {
            mPurchase = null;
            purchase(mSku);
            DebugTool.logD("CONSUME SUCCESS + " + result.toString());
        }
    }

    private class PurchaseListener extends EmptyRequestListener<Purchase> {

        @Override
        public void onSuccess(@Nonnull final Purchase purchase) {
            mPurchase = purchase;

            MorePhonePurchase morePhonePurchase = new MorePhonePurchase(
                    MyPreference.getUserId(getContext()),
                    purchase.packageName,
                    purchase.token,
                    purchase.state.id,
                    purchase.orderId,
                    purchase.time,
                    purchase.sku);

            showProgressForever();
            ApiMorePhone.purchase(getContext(), morePhonePurchase, new Callback<MorePhonePurchase>() {
                @Override
                public void onResponse(Call<MorePhonePurchase> call, Response<MorePhonePurchase> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().productId.equals("add_fund")) {
                            mBalanceAdd += Constant.PRODUCT_ADD_FUND;
                        }
                    }
                    hideProgressForever();
                }

                @Override
                public void onFailure(Call<MorePhonePurchase> call, Throwable t) {
                    hideProgressForever();
                }
            });
            DebugTool.logD("PURCHASE SUCCESS + " + purchase.toJson());
        }
    }

    private class InventoryCallback implements Inventory.Callback {

        private Sku mSku;

        public InventoryCallback(Sku sku) {
            mSku = sku;
        }

        @Override
        public void onLoaded(@Nonnull Inventory.Products products) {
            final Inventory.Product product = products.get(ProductTypes.IN_APP);
            DebugTool.logD("product: " + product.id);

            if (!product.supported) {
                return;
            }
            mPurchase = product.getPurchaseInState(mSku, Purchase.State.PURCHASED);

            purchase(mSku);

//
//            onPurchaseChanged();
//            mButtonPayNow.setEnabled(true);
        }

    }

    private void purchase(final Sku sku) {
        if (mPurchase == null) {
            DebugTool.logD("PURCHASE NULL");
//            mUiCheckout.startPurchaseFlow(sku, null, new PurchaseListener());
//            mUiCheckout.startPurchaseFlow(ProductTypes.IN_APP, skus, null, new PurchaseListener());
        } else {
            DebugTool.logD("PURCHASE OK");
//            mUiCheckout.whenReady(new Checkout.EmptyListener() {
//                @Override
//                public void onReady(@Nonnull BillingRequests requests) {
//                    requests.consume(mPurchase.token, new ConsumeListener(sku));
//                }
//            });
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
