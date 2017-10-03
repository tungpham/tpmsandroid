package com.ethan.morephone.presentation.dashboard.expire;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.morephone.data.database.PhoneNumberDatabaseHelper;
import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.DateUtils;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.purchase.ChooseDateDialog;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TimerListener;
import com.ethan.morephone.widget.countdown.CountDownView;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/20/17.
 */

public class ExpireFragment extends BaseFragment implements View.OnClickListener,
        ChooseDateDialog.ChooseDateDialogListener {

    public static ExpireFragment getInstance(Bundle bundle) {
        ExpireFragment fragment = new ExpireFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView mTextPurchasePrice;
    private TextView mTextPurchaseExpireSummary;

    private long mCurrentDate = System.currentTimeMillis();
    private long mMinDate = mCurrentDate;

    private String mPhoneNumberId;
    private PhoneNumber mPhoneNumberDTO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expire, container, false);
        String phoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        mPhoneNumberDTO = PhoneNumberDatabaseHelper.findPhoneNumber(getContext(), phoneNumber);

        long duration = mPhoneNumberDTO.getExpire() - System.currentTimeMillis();
        mMinDate = mCurrentDate = mPhoneNumberDTO.getExpire();

        CountDownView countDownView = (CountDownView) view.findViewById(R.id.count_down_view);
        countDownView.setInitialTime(duration); // Initial time of 30 seconds.
        countDownView.start();
        countDownView.setListener(new TimerListener() {
            @Override
            public void timerElapsed() {
                PhoneService.startServiceUnregisterPhoneNumber(getContext(), mPhoneNumberDTO.getPhoneNumber(), mPhoneNumberDTO.getSid());
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        mTextPurchasePrice = (TextView) view.findViewById(R.id.text_purchase_price);
        mTextPurchaseExpireSummary = (TextView) view.findViewById(R.id.text_purchase_expire_summary);

        mTextPurchaseExpireSummary.setText(Utils.formatDatePurchase(mCurrentDate));

        view.findViewById(R.id.relative_expire).setOnClickListener(this);
        view.findViewById(R.id.button_purchase_expire_now).setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_expire:
                ChooseDateDialog recordDialog = ChooseDateDialog.getInstance(mCurrentDate, mMinDate);
                recordDialog.setCancelable(false);
                recordDialog.show(getChildFragmentManager(), ChooseDateDialog.class.getSimpleName());
                recordDialog.setChooseDateDialogListener(this);
                break;
            case R.id.button_purchase_expire_now:
                long diffDate = DateUtils.getDifferenceDays(new Date(mMinDate), new Date(mCurrentDate));
                if (diffDate < 1) {
                    Toast.makeText(getContext(), getString(R.string.purchase_expire_date_alert), Toast.LENGTH_SHORT).show();
                } else {
                    ApiMorePhone.updateExpire(getContext(), mPhoneNumberId, MyPreference.getUserId(getContext()) , mCurrentDate, new Callback<BaseResponse<PhoneNumber>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), getString(R.string.purchase_expire_update_success), Toast.LENGTH_SHORT).show();
                                mPhoneNumberDTO.setExpire(mCurrentDate);
                                mMinDate = mCurrentDate;
                                PhoneNumberDatabaseHelper.updatePhoneNumber(getContext(), mPhoneNumberDTO);
                            } else {
                                Toast.makeText(getContext(), getString(R.string.purchase_expire_update_fail) + "22", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                            Toast.makeText(getContext(), getString(R.string.purchase_expire_update_fail), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void chooseDate(long date) {
        mCurrentDate = date;
        mTextPurchaseExpireSummary.setText(Utils.formatDatePurchase(date));
        long diffDate = DateUtils.getDifferenceDays(new Date(mMinDate), new Date(date));
        mTextPurchasePrice.setText(getResources().getQuantityString(R.plurals.credits, (int) diffDate, String.valueOf(diffDate * Constant.CREDIT_BUY_PHONE)));
    }
}
