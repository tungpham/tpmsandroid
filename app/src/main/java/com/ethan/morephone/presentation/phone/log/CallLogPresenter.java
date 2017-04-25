package com.ethan.morephone.presentation.phone.log;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.network.ApiMorePhone;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/25/17.
 */

public class CallLogPresenter implements CallLogContract.Presenter {

    private CallLogContract.View mView;

    public CallLogPresenter(@NonNull CallLogContract.View view){
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCallLogs(Context context) {
        mView.showLoading(true);
        ApiMorePhone.getCallLogs(context, Constant.ACCOUNT_SID, MyPreference.getPhoneNumber(context), new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                mView.showCallLog(response.body());
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<Calls> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }
}
