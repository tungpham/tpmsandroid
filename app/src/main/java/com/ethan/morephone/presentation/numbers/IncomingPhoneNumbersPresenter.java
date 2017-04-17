package com.ethan.morephone.presentation.numbers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.DeleteIncomingPhoneNumber;
import com.ethan.morephone.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersPresenter implements IncomingPhoneNumbersContract.Presenter {

    private final IncomingPhoneNumbersContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final DeleteIncomingPhoneNumber mDeleteIncomingPhoneNumber;

    public IncomingPhoneNumbersPresenter(@NonNull IncomingPhoneNumbersContract.View view,
                                         @NonNull UseCaseHandler useCaseHandler,
                                         @NonNull DeleteIncomingPhoneNumber deleteIncomingPhoneNumber) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mDeleteIncomingPhoneNumber = deleteIncomingPhoneNumber;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void getFakeData(Context context) {
        mView.showLoading(true);
        ApiManager.fakeData(context, new Callback<FakeData>() {
            @Override
            public void onResponse(Call<FakeData> call, Response<FakeData> response) {
                mView.showLoading(false);
                if (response.isSuccessful()) {
                    FakeData fakeData = response.body();
                    mView.showFakeData(fakeData);
//                    mView.showPhoneNumbers(fakeData.list_number);
                }
            }

            @Override
            public void onFailure(Call<FakeData> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void deleteIncomingPhoneNumber(String incomingPhoneNumberSid) {
        DeleteIncomingPhoneNumber.RequestValue requestValue = new DeleteIncomingPhoneNumber.RequestValue(Constant.ACCOUNT_SID, incomingPhoneNumberSid);
        mUseCaseHandler.execute(mDeleteIncomingPhoneNumber, requestValue, new UseCase.UseCaseCallback<DeleteIncomingPhoneNumber.ResponseValue>() {
            @Override
            public void onSuccess(DeleteIncomingPhoneNumber.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadIncomingPhoneNumbers(Context context) {
        mView.showLoading(true);
        ApiManager.getIncomingPhoneNumbers(context, new Callback<IncomingPhoneNumbers>() {
            @Override
            public void onResponse(Call<IncomingPhoneNumbers> call, Response<IncomingPhoneNumbers> response) {
                if (response.isSuccessful()) {
                    IncomingPhoneNumbers incomingPhoneNumbers = response.body();
                    if (incomingPhoneNumbers != null && incomingPhoneNumbers.incomingPhoneNumbers != null && !incomingPhoneNumbers.incomingPhoneNumbers.isEmpty()) {
                        mView.showPhoneNumbers(response.body().incomingPhoneNumbers);
                    }
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<IncomingPhoneNumbers> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

}
