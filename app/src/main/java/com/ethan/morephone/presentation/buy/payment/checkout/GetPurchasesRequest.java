package com.ethan.morephone.presentation.buy.payment.checkout;

import android.os.Bundle;
import android.os.RemoteException;

import com.android.morephone.data.log.DebugTool;
import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.ethan.morephone.presentation.buy.payment.checkout.ResponseCodes.EXCEPTION;

final class GetPurchasesRequest extends Request<Purchases> {

    @Nonnull
    private final String mProduct;

    @Nullable
    private final String mContinuationToken;

    @Nonnull
    private final PurchaseVerifier mVerifier;

    GetPurchasesRequest(@Nonnull String product, @Nullable String continuationToken, @Nonnull PurchaseVerifier verifier) {
        super(RequestType.GET_PURCHASES);
        mProduct = product;
        mContinuationToken = continuationToken;
        mVerifier = verifier;
    }

    GetPurchasesRequest(@Nonnull GetPurchasesRequest request, @Nonnull String continuationToken) {
        super(RequestType.GET_PURCHASES, request);
        mProduct = request.mProduct;
        mContinuationToken = continuationToken;
        mVerifier = request.mVerifier;
    }

    @Nonnull
    String getProduct() {
        return mProduct;
    }

    @Nullable
    String getContinuationToken() {
        return mContinuationToken;
    }

    @Override
    void start(@Nonnull IInAppBillingService service, @Nonnull String packageName) throws RemoteException {
        final Bundle bundle = service.getPurchases(mApiVersion, packageName, mProduct, mContinuationToken);
        if (handleError(bundle)) {
            return;
        }
        try {
            final String continuationToken = Purchases.getContinuationTokenFromBundle(bundle);
            final List<Purchase> purchases = Purchases.getListFromBundle(bundle);
            if (purchases.isEmpty()) {
                onSuccess(new Purchases(mProduct, purchases, continuationToken));
                return;
            }
            final VerificationListener listener = new VerificationListener(this, mProduct, continuationToken);
            mVerifier.verify(purchases, listener);
            if (!listener.mCalled) {
                listener.onError(EXCEPTION, new IllegalStateException("Either onSuccess or onError methods must be called by PurchaseVerifier"));
            }
        } catch (JSONException e) {
            onError(e);
        }
    }

    @Nullable
    @Override
    protected String getCacheKey() {
        if (mContinuationToken != null) {
            return mProduct + "_" + mContinuationToken;
        } else {
            return mProduct;
        }
    }

    private static class VerificationListener implements RequestListener<List<Purchase>> {
        @Nonnull
        private final Request<Purchases> mRequest;
        @Nonnull
        private final String mProduct;
        @Nullable
        private final String mContinuationToken;
        @Nonnull
        private final Thread mOriginalThread;
        private boolean mCalled;

        public VerificationListener(@Nonnull Request<Purchases> request, @Nonnull String product, @Nullable String continuationToken) {
            mRequest = request;
            mProduct = product;
            mContinuationToken = continuationToken;
            mOriginalThread = Thread.currentThread();
            DebugTool.logD("mContinuationToken = " + mContinuationToken);
        }

        @Override
        public void onSuccess(@Nonnull List<Purchase> verifiedPurchases) {
            Check.equals(mOriginalThread, Thread.currentThread(), "Must be called on the same thread");
            mCalled = true;
            mRequest.onSuccess(new Purchases(mProduct, verifiedPurchases, mContinuationToken));
            DebugTool.logD("SUCCESS = ");
        }

        @Override
        public void onError(int response, @Nonnull Exception e) {
            Check.equals(mOriginalThread, Thread.currentThread(), "Must be called on the same thread");
            mCalled = true;
            if (response == EXCEPTION) {
                mRequest.onError(e);
            } else {
                DebugTool.logD("FAIL = ");
                mRequest.onError(response);
            }
        }
    }
}
