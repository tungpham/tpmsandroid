package com.ethan.morephone.presentation.authentication;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 6/26/17.
 */

public interface AuthenticationContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void nextActivity();
    }

    interface Presenter extends BasePresenter {

    }

}
