
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

final class RequestException extends Exception {

    RequestException(@Nonnull Exception cause) {
        super(cause);
    }
}
