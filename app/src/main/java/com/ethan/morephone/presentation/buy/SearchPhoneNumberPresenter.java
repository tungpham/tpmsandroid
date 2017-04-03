package com.ethan.morephone.presentation.buy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.GetAvailableCountries;

/**
 * Created by Ethan on 4/3/17.
 */

public class SearchPhoneNumberPresenter implements SearchPhoneNumberContract.Presenter {

    private final SearchPhoneNumberContract.View mView;

    private final UseCaseHandler mUseCaseHandler;
    private final GetAvailableCountries mGetAvailableCountries;

    public SearchPhoneNumberPresenter(@NonNull SearchPhoneNumberContract.View view,
                                      @NonNull UseCaseHandler useCaseHandler,
                                      @NonNull GetAvailableCountries getAvailableCountries){
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetAvailableCountries = getAvailableCountries;

        mView.setPresenter(this);
    }
    @Override
    public void loadAvailableCountries(Context context) {
        mView.showLoading(true);

        GetAvailableCountries.RequestValue requestValue = new GetAvailableCountries.RequestValue();
        mUseCaseHandler.execute(mGetAvailableCountries, requestValue, new UseCase.UseCaseCallback<GetAvailableCountries.ResponseValue>() {
            @Override
            public void onSuccess(GetAvailableCountries.ResponseValue response) {
                mView.showLoading(false);
                mView.showAvailableCountries(response.getAvailableCountries().countries);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
