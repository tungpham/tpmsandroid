
package com.ethan.morephone.presentation.buy.payment.checkout;

import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.ethan.morephone.presentation.buy.payment.checkout.RequestType.BILLING_SUPPORTED;

final class BillingSupportedRequest extends Request<Object> {

    @Nonnull
    private final String mProduct;

    BillingSupportedRequest(@Nonnull String product) {
        this(product, Billing.V3);
    }

    BillingSupportedRequest(@Nonnull String product, int apiVersion) {
        super(BILLING_SUPPORTED, apiVersion);
        mProduct = product;
    }

    @Override
    public void start(@Nonnull IInAppBillingService service, @Nonnull String packageName) throws RemoteException {
        final int response = service.isBillingSupported(mApiVersion, packageName, mProduct);
        if (!handleError(response)) {
            onSuccess(new Object());
        }
    }

    @Nullable
    @Override
    protected String getCacheKey() {
        if (mApiVersion == Billing.V3) {
            return mProduct;
        }
        return mProduct + "_" + mApiVersion;
    }
}
