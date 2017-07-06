package com.ethan.morephone.presentation.buy.payment.purchase;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.utils.Injection;

/**
 * Created by Ethan on 5/4/17.
 */

public class PurchaseFragment extends BaseFragment implements
        PaymentMethodsDialog.PaymentMethodsListener,
        View.OnClickListener,
        PurchaseContract.View {

    public static PurchaseFragment getInstance(Bundle bundle) {
        PurchaseFragment purchaseFragment = new PurchaseFragment();
        purchaseFragment.setArguments(bundle);
        return purchaseFragment;
    }

    private PurchaseContract.Presenter mPresenter;

    private String mPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PurchasePresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerBuyIncomingPhoneNumber(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        view.findViewById(R.id.button_purchase_pay_now).setOnClickListener(this);

        Bundle bundle = getArguments();

        mPhoneNumber = bundle.getString(PurchaseActivity.BUNDLE_PHONE_NUMBER);

        TextView textPhoneNumber = (TextView) view.findViewById(R.id.text_purchase_phone_number);
        textPhoneNumber.setText(bundle.getString(PurchaseActivity.BUNDLE_FRIENDLY_PHONE_NUMBER));

        ImageView imageVoice = (ImageView) view.findViewById(R.id.image_purchase_voice);
        TextView textVoice = (TextView) view.findViewById(R.id.text_purchase_voice);
        TextView textVoiceSummary = (TextView) view.findViewById(R.id.text_purchase_voice_summary);

        ImageView imageMessage = (ImageView) view.findViewById(R.id.image_purchase_message);
        TextView textMessage = (TextView) view.findViewById(R.id.text_purchase_message);
        TextView textMessageSummary = (TextView) view.findViewById(R.id.text_purchase_message_summary);

        ImageView imageMMS = (ImageView) view.findViewById(R.id.image_purchase_mms);
        TextView textMMS = (TextView) view.findViewById(R.id.text_purchase_mms);
        TextView textMMSSummary = (TextView) view.findViewById(R.id.text_purchase_mms_summary);

        ImageView imageFax = (ImageView) view.findViewById(R.id.image_purchase_fax);
        TextView textFax = (TextView) view.findViewById(R.id.text_purchase_fax);
        TextView textFaxSummary = (TextView) view.findViewById(R.id.text_purchase_fax_summary);

        if (!bundle.getBoolean(PurchaseActivity.BUNDLE_IS_VOICE)) {
            imageVoice.setVisibility(View.GONE);
            textVoice.setVisibility(View.GONE);
            textVoiceSummary.setVisibility(View.GONE);
        }

        if (!bundle.getBoolean(PurchaseActivity.BUNDLE_IS_SMS)) {
            imageMessage.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);
            textMessageSummary.setVisibility(View.GONE);
        }

        if (!bundle.getBoolean(PurchaseActivity.BUNDLE_IS_MMS)) {
            imageMMS.setVisibility(View.GONE);
            textMMS.setVisibility(View.GONE);
            textMMSSummary.setVisibility(View.GONE);
        }

        if (!bundle.getBoolean(PurchaseActivity.BUNDLE_IS_FAX)) {
            imageFax.setVisibility(View.GONE);
            textFax.setVisibility(View.GONE);
            textFaxSummary.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onChoosePaymentMethods(int paymentMethods) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_purchase_pay_now:
                DebugTool.logD("PHONE NUMBER: " + mPhoneNumber);
                mPresenter.buyIncomingPhoneNumber(getContext(), "abcdef");
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isAdded())
            if (isActive) {
                showProgress();
            } else {
                hideProgress();
            }
    }

    @Override
    public void buyIncomingPhoneNumberSuccess(IncomingPhoneNumber incomingPhoneNumber) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void buyIncomingPhoneNumberFail() {
        Toast.makeText(getContext(), getString(R.string.purchase_fail), Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setPresenter(PurchaseContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
