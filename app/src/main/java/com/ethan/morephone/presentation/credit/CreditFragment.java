package com.ethan.morephone.presentation.credit;

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
import android.widget.TextView;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.network.HTTPStatus;
import com.ethan.morephone.MyApplication;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.card.CardActivity;
import com.ethan.morephone.presentation.buy.payment.card.CreditCardUtils;
import com.ethan.morephone.presentation.buy.payment.checkout.ActivityCheckout;
import com.ethan.morephone.presentation.buy.payment.checkout.Billing;
import com.ethan.morephone.presentation.buy.payment.checkout.BillingRequests;
import com.ethan.morephone.presentation.buy.payment.checkout.Checkout;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.PurchaseFlow;
import com.ethan.morephone.presentation.buy.payment.checkout.RequestListener;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.fund.AddFundActivity;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.AvailableSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.PurchasedSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.adapter.TargetSkusAdapter;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;
import com.ethan.morephone.presentation.buy.payment.purchase.PaymentMethodsDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.SUBSCRIPTION;

/**
 * Created by Ethan on 5/10/17.
 */

public class CreditFragment extends BaseFragment implements
        View.OnClickListener{


    public static CreditFragment getInstance() {
        return new CreditFragment();
    }


    private TextView mTextTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        view.findViewById(R.id.button_add_fund).setOnClickListener(this);

        mTextTitle = (TextView) view.findViewById(R.id.text_credit_title);

        showProgress();
        ApiMorePhone.usage(getContext(), MyPreference.getUserId(getContext()), new Callback<BaseResponse<UsageItem>>() {
            @Override
            public void onResponse(Call<BaseResponse<UsageItem>> call, Response<BaseResponse<UsageItem>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == HTTPStatus.OK.getStatusCode()){
                        mTextTitle.setText(getString(R.string.credit_title)+  (double)Math.round(response.body().getResponse().getBalance() * 100) / 100);
                    }
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<BaseResponse<UsageItem>> call, Throwable t) {
                hideProgress();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_fund:
                startActivity(new Intent(getActivity(), AddFundActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
//        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }


}
