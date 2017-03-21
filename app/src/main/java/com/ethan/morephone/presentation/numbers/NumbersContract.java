package com.ethan.morephone.presentation.numbers;

import android.content.Context;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.NumberEntity;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public interface NumbersContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showPhoneNumbers(List<NumberEntity> numberEntities);

        void showFakeData(FakeData fakeData);
    }

    interface Presenter extends BasePresenter {

        void getFakeData(Context context);

    }
}
