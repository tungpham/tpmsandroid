package com.ethan.morephone.presentation.phone.log;

import com.android.morephone.data.entity.CallEntity;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 3/21/17.
 */

public interface CallLogContract {

    interface View extends BaseView<Presenter> {

        void showCallLog(List<CallEntity> callEntities);

    }

    interface Presenter extends BasePresenter {

    }
}
