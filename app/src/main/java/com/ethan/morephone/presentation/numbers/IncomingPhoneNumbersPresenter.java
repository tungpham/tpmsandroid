package com.ethan.morephone.presentation.numbers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.morephone.data.database.ContactDatabaseHelper;
import com.android.morephone.data.database.PhoneNumberDatabaseHelper;
import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.network.HTTPStatus;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.DeleteIncomingPhoneNumber;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.phone.service.PhoneService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersPresenter implements IncomingPhoneNumbersContract.Presenter {

    private final IncomingPhoneNumbersContract.View mView;

    public IncomingPhoneNumbersPresenter(@NonNull IncomingPhoneNumbersContract.View view,
                                         @NonNull UseCaseHandler useCaseHandler,
                                         @NonNull DeleteIncomingPhoneNumber deleteIncomingPhoneNumber) {
        mView = view;
//        UseCaseHandler mUseCaseHandler = useCaseHandler;
//        DeleteIncomingPhoneNumber mDeleteIncomingPhoneNumber = deleteIncomingPhoneNumber;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void deleteIncomingPhoneNumber(final Context context, final String phoneNumber, final String incomingPhoneNumberSid, final String phoneNumberId) {
        ApiMorePhone.deletePhoneNumber(context, incomingPhoneNumberSid, TwilioManager.getSid(context), TwilioManager.getAuthCode(context), new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == HTTPStatus.OK.getStatusCode()) {
                    Toast.makeText(context, context.getString(R.string.delete_phone_number_success), Toast.LENGTH_SHORT).show();
                    PhoneService.startServiceUnregisterPhoneNumber(context, phoneNumber, incomingPhoneNumberSid);

//                    Set<String> phoneNumberUsage = MyPreference.getPhoneNumberUsage(context);
//                    phoneNumberUsage.remove(phoneNumber);
//                    MyPreference.setPhoneNumberUsage(context, phoneNumberUsage);

                    PhoneNumberDatabaseHelper.deletePhoneNumber(context, incomingPhoneNumberSid);
                    ContactDatabaseHelper.deleteAllContact(context, phoneNumberId);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadIncomingPhoneNumbers(final Context context) {
        mView.showLoading(true);
        DebugTool.logD("LOAD INCOMING PHONE");

        ApiMorePhone.getPhoneNumbers(context, MyPreference.getUserId(context), new Callback<BaseResponse<List<PhoneNumber>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PhoneNumber>>> call, Response<BaseResponse<List<PhoneNumber>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null && !response.body().getResponse().isEmpty()) {
                    PhoneNumberDatabaseHelper.deleteAllPhoneNumber(context);
                    for (final PhoneNumber incomingPhoneNumber : response.body().getResponse()) {
                        PhoneService.startServiceRegisterPhoneNumber(context, incomingPhoneNumber);
                    }

                    mView.showPhoneNumbers(response.body().getResponse());
                } else {
                    mView.emptyPhoneNumber();
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PhoneNumber>>> call, Throwable t) {
                mView.showLoading(false);
            }
        });
//        ApiManager.getIncomingPhoneNumbers(context, new Callback<IncomingPhoneNumbers>() {
//            @Override
//            public void onResponse(Call<IncomingPhoneNumbers> call, Response<IncomingPhoneNumbers> response) {
//                if (response.isSuccessful()) {
//                    IncomingPhoneNumbers incomingPhoneNumbers = response.body();
//                    if (incomingPhoneNumbers != null && incomingPhoneNumbers.incomingPhoneNumbers != null && !incomingPhoneNumbers.incomingPhoneNumbers.isEmpty()) {
//
//                        for (final IncomingPhoneNumber incomingPhoneNumber : incomingPhoneNumbers.incomingPhoneNumbers) {
//                            PhoneService.startServiceWithAction(context, PhoneService.ACTION_REGISTER_PHONE_NUMBER, incomingPhoneNumber.phoneNumber, "");
//                        }
//
//
//                        mView.showPhoneNumbers(incomingPhoneNumbers.incomingPhoneNumbers);
//                    } else {
//                        mView.emptyPhoneNumber();
//                    }
//                } else {
//                    mView.emptyPhoneNumber();
//                }
//                mView.showLoading(false);
//            }
//
//            @Override
//            public void onFailure(Call<IncomingPhoneNumbers> call, Throwable t) {
//                mView.showLoading(false);
//            }
//        });
    }

}
