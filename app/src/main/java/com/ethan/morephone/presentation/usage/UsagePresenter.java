package com.ethan.morephone.presentation.usage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.usage.Usage;
import com.android.morephone.data.network.ApiMorePhone;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/26/17.
 */

public class UsagePresenter implements UsageContract.Presenter {

    private final UsageContract.View mView;

    public UsagePresenter(@NonNull UsageContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadUsage(Context context) {
        mView.showLoading(true);
        ApiMorePhone.getUsage(context, Constant.ACCOUNT_SID, MyPreference.getPhoneNumberSid(context), new Callback<Usage>() {
            @Override
            public void onResponse(Call<Usage> call, Response<Usage> response) {
                if(response.isSuccessful() && response.body() != null){
                    mView.showUsage(response.body());
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<Usage> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
