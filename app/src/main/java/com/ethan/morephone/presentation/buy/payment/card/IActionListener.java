package com.ethan.morephone.presentation.buy.payment.card;

import com.ethan.morephone.presentation.buy.payment.card.fragment.CreditCardFragment;

/**
 * Created by Ethan on 5/3/17.
 */

public interface IActionListener {
    public void onActionComplete(CreditCardFragment fragment);
    public void onEdit(CreditCardFragment fragment, String edit);

}